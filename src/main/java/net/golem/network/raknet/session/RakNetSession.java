package net.golem.network.raknet.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.AcknowledgePacket;
import net.golem.network.raknet.protocol.DisconnectionNotificationPacket;
import net.golem.network.raknet.protocol.RakNetDatagram;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.connection.connected.ConnectedPingPacket;
import net.golem.network.raknet.types.PacketReliability;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j2
public class RakNetSession implements Session {

	/**
	 * Update ever 10ms
	 */
	public static final int UPDATE_PERIOD = 10;

	protected RakNetServer server;

	protected int maximumTransferUnits;

	protected long globalUniqueId;

	private long lastTimestamp = System.currentTimeMillis();

	private int latency = -1;

	protected SessionManager handler;
	protected ChannelHandlerContext context;
	private ScheduledFuture<?> task;

	protected InetSocketAddress address;

	protected boolean active = false;

	protected SessionState state;

	protected ArrayList<SessionListener> listeners = new ArrayList<>();

	protected ConcurrentLinkedQueue<DataPacket> packetQueue = new ConcurrentLinkedQueue<>();

	public RakNetSession(RakNetServer server, SessionManager handler, InetSocketAddress address, ChannelHandlerContext context) {
		this.server = server;
		this.handler = handler;
		this.address = address;
		this.context = context;
	}

	public RakNetServer getServer() {
		return server;
	}

	public int getLatency() {
		return latency;
	}

	public void setLatency(int latency) {
		this.latency = latency;
	}

	public SessionManager getHandler() {
		return handler;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public ChannelHandlerContext getContext() {
		return context;
	}

	public SessionState getState() {
		return state;
	}

	public void setState(SessionState state) {
		this.state = state;
	}

	public ArrayList<SessionListener> getListeners() {
		return listeners;
	}

	public void addListener(SessionListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeListener(SessionListener listener) {
		listeners.remove(listener);
	}

	public ConcurrentLinkedQueue<DataPacket> getPacketQueue() {
		return packetQueue;
	}

	public void ping(int reliability) {
		ConnectedPingPacket packet = new ConnectedPingPacket();
		packet.pingTime = this.getServer().getRakNetTimeMS();
	}

	public void ping() {
		this.ping(PacketReliability.UNRELIABLE.getId());
	}

	@Override
	public void close(String reason) {
		log.info(String.format("Closing session due to %s", reason));
		this.sendPacket(new DisconnectionNotificationPacket());
		this.getHandler().remove(this);
	}

	@Override
	public void close() {
		this.close("Unknown");
	}

	@Override
	public int getMaximumTransferUnits() {
		return maximumTransferUnits;
	}

	@Override
	public void setMaximumTransferUnits(int size) {
		this.maximumTransferUnits = size;
	}

	@Override
	public void update() {

	}

	private void tick() {
		/**
		 * Don't try to update if the session is closed
		 */
		if(this.getState() == SessionState.CLOSED) {
			task.cancel(true);
		}

	}

	@Override
	public void handle(DataPacket packet) {
		if(packet instanceof RakNetDatagram) {
			this.sendPacket(AcknowledgePacket.createACK());
		}
	}

	@Override
	public void sendPacket(DataPacket packet, boolean direct) {

	}

	@Override
	public void sendPacket(DataPacket packet) {
		PacketEncoder encoder = new PacketEncoder();
		this.getContext().writeAndFlush(new DatagramPacket(packet.write(encoder), this.getAddress()));
	}

	@Override
	public void sendPacket(RakNetPacket packet, PacketReliability reliability) {

	}

	@Override
	public void sendPacket(RakNetPacket packet, PacketReliability reliability, boolean immediate) {

	}
}
