package net.golem.network.raknet.session;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.*;
import net.golem.network.raknet.protocol.connection.ConnectionRequestAcceptedPacket;
import net.golem.network.raknet.protocol.connection.ConnectionRequestPacket;
import net.golem.network.raknet.protocol.connection.connected.ConnectedPingPacket;
import net.golem.network.raknet.session.codec.SessionDecodeLayer;
import net.golem.network.raknet.session.codec.SessionEncodeLayer;
import net.golem.network.raknet.types.PacketPriority;
import net.golem.network.raknet.types.PacketReliability;
import net.golem.network.raknet.protocol.connection.connected.ConnectedPongPacket;

import javax.annotation.Nonnegative;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Log4j2
public class RakNetSession {

	public static final long STALE_TIME = 5000L;
	public static final long TIMEOUT_TIME = 15000L;

	public static final long PING_TIME = 5000L;

	/**
	 * Update every 10ms
	 */
	public static final int UPDATE_PERIOD = 10;

	protected RakNetServer server;

	protected int maximumTransferUnits;

	protected long globalUniqueId;

	private long lastPingTime = System.currentTimeMillis();
	private long lastReceivedTime = System.currentTimeMillis();
	private long lastTimestamp = System.currentTimeMillis();
	private int latency = -1;

	protected SessionManager handler;
	protected ChannelHandlerContext context;

	protected InetSocketAddress address;

	protected boolean active = true;
	protected boolean closed = false;

	protected SessionState state = SessionState.INITIALIZING;

	protected SessionDecodeLayer decodeLayer = new SessionDecodeLayer(this);
	protected SessionEncodeLayer encodeLayer = new SessionEncodeLayer(this);

	protected NioEventLoopGroup group = new NioEventLoopGroup();

	public RakNetSession(RakNetServer server, SessionManager handler, InetSocketAddress address, ChannelHandlerContext context) {
		this.server = server;
		this.handler = handler;
		this.address = address;
		this.context = context;
		group.scheduleAtFixedRate(this::tick, 0, UPDATE_PERIOD, TimeUnit.MILLISECONDS);
		log.info("Session created: [{}]", address);
	}

	public SessionDecodeLayer getDecodeLayer() {
		return decodeLayer;
	}

	public SessionEncodeLayer getEncodeLayer() {
		return encodeLayer;
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


	public void ping(PacketReliability reliability) {
		ConnectedPingPacket packet = new ConnectedPingPacket();
		packet.pingTime = this.getServer().getRakNetTimeMS();
		encodeLayer.sendEncapsulatedPacket(packet, reliability, PacketPriority.IMMEDIATE, 0);
		this.lastPingTime = System.currentTimeMillis();
	}

	public void ping() {
		this.ping(PacketReliability.UNRELIABLE);
	}

	public void onConnectedPing(ConnectedPingPacket packet) {
		ConnectedPongPacket pk = new ConnectedPongPacket();
		pk.sendPingTime = packet.pingTime;
		pk.sendPongTime = this.getServer().getRakNetTimeMS();
		encodeLayer.sendEncapsulatedPacket(pk);
	}

	public void onPong() {
		this.lastTimestamp = System.currentTimeMillis();
	}

	public boolean isStale() {
		return System.currentTimeMillis() - this.lastReceivedTime >= STALE_TIME;
	}

	public boolean isTimedOut() {
		return System.currentTimeMillis() - this.lastReceivedTime >= TIMEOUT_TIME;
	}

	public void close(String reason) {
		if(this.closed) {
			return;
		}
		this.closed = true;
		decodeLayer.close();
		encodeLayer.close();
		this.disconnect();
		this.group.shutdownGracefully();
		this.getHandler().remove(this);
		this.onClosed();
		log.info("Closed session [{}] due to {}", getAddress(), reason);
	}

	public void close() {
		this.close("Unknown");
	}

	public void disconnect() {
		this.setState(SessionState.DISCONNECTED);
		decodeLayer.disconnect();
		encodeLayer.disconnect();
	}

	public boolean isClosed() {
		return closed;
	}

	public boolean isActive() {
		return active;
	}

	protected void onClosed() {

	}

	public int getMaximumTransferUnits() {
		return maximumTransferUnits;
	}

	public void setMaximumTransferUnits(int size) {
		this.maximumTransferUnits = size;
	}

	public void tick() {
		if(this.isClosed()) {
			return;
		}
		long currentTime = System.currentTimeMillis();
		this.update(currentTime);
	}

	private void update(long currentTime) {
		if(this.isTimedOut()) {
			this.close("timeout");
			return;
		}

		if(this.isStale() && this.active) {
			log.info("Stale session: [{}]", this.getAddress());
			this.active = false;
		}
		/**
		 * Don't try to update if the session is closed
		 */
		if(this.getState() == SessionState.DISCONNECTED) {
			return;
		}


		if(currentTime - this.lastPingTime > PING_TIME) {
			this.ping();
		}

		decodeLayer.update();
		encodeLayer.update();
	}

	public void updateReceivedTime() {
		this.lastReceivedTime = System.currentTimeMillis();
	}

	public void handle(DataPacket packet) {
		this.updateReceivedTime();
		if(packet instanceof ConnectionRequestPacket) {
			ConnectionRequestAcceptedPacket pk = new ConnectionRequestAcceptedPacket();
			pk.clientAddress = this.getAddress();
			pk.sendPingTime = ((ConnectionRequestPacket) packet).sendPingTime;
			pk.sendPongTime = this.getServer().getRakNetTimeMS();
			encodeLayer.sendEncapsulatedPacket(pk, PacketReliability.UNRELIABLE, PacketPriority.IMMEDIATE, 0);
			return;
		}
		if(packet instanceof DisconnectionNotificationPacket) {
			return;
		}
		if(packet instanceof ConnectedPingPacket) {
			this.onConnectedPing((ConnectedPingPacket) packet);

		}
	}

	public void handleACK(AcknowledgePacket packet) {
		this.updateReceivedTime();
	}

	public void handleNAK(AcknowledgePacket packet) {
		this.updateReceivedTime();
	}

	public void sendPacket(DataPacket packet, PacketReliability reliability, boolean immediate) {
		if(this.isClosed()) {
			return;
		}
		PacketEncoder encoder = new PacketEncoder();
		this.getContext().writeAndFlush(new DatagramPacket(packet.write(encoder), this.getAddress()));
	}

	public void sendPacket(DataPacket packet, boolean immediate) {
		sendPacket(packet, PacketReliability.UNRELIABLE, immediate);
	}

	public void sendPacket(DataPacket packet) {
		sendPacket(packet, false);
	}

}