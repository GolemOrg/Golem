package net.golem.network.raknet.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.types.PacketReliability;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class RakNetSession implements Session {

	/**
	 * Update ever 10ms
	 */
	public static final int UPDATE_PERIOD = 10;

	protected int maximumTransferUnits;
	protected long globalUniqueId;

	private long lastTimestamp = System.currentTimeMillis();
	private int latency = -1;

	protected SessionHandler handler;
	protected ChannelHandlerContext context;
	private ScheduledFuture<?> task;

	protected InetSocketAddress address;
	protected SessionState state;

	protected ArrayList<SessionListener> listeners = new ArrayList<>();

	protected ConcurrentLinkedQueue<DataPacket> packetQueue = new ConcurrentLinkedQueue<>();

	public RakNetSession(SessionHandler handler, InetSocketAddress address, ChannelHandlerContext context) {
		this.handler = handler;
		this.address = address;
		this.context = context;
		this.task = handler.getSessionGroup().scheduleAtFixedRate(this::tick, 0, UPDATE_PERIOD, TimeUnit.MILLISECONDS);
	}

	public int getLatency() {
		return latency;
	}

	public void setLatency(int latency) {
		this.latency = latency;
	}

	public SessionHandler getHandler() {
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

	@Override
	public void close(String reason) {

	}

	@Override
	public void close() {

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

	}

	@Override
	public void sendPacket(DataPacket packet, boolean direct) {

	}

	@Override
	public void sendPacket(DataPacket packet) {

	}

	@Override
	public void sendPacket(RakNetPacket packet, PacketReliability reliability) {

	}

	@Override
	public void sendPacket(RakNetPacket packet, PacketReliability reliability, boolean immediate) {

	}
}
