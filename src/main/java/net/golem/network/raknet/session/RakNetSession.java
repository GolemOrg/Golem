package net.golem.network.raknet.session;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.*;
import net.golem.network.raknet.protocol.connection.ConnectionRequestAcceptedPacket;
import net.golem.network.raknet.protocol.connection.ConnectionRequestPacket;
import net.golem.network.raknet.protocol.connection.connected.ConnectedPingPacket;
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
public class RakNetSession implements Session {

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

	private int sendSequenceNumber = 0;

	private int latency = -1;

	protected SessionManager handler;
	protected ChannelHandlerContext context;

	protected InetSocketAddress address;

	protected boolean active = true;
	protected boolean closed = false;

	protected SessionState state = SessionState.INITIALIZING;

	protected Set<Integer> ackQueue = new HashSet<>();
	protected int highestSequenceNumber = -1;

	protected ArrayList<SessionListener> listeners = new ArrayList<>();


	protected LinkedHashMap<Integer, DataPacket> needACKs = new LinkedHashMap<>();

	protected ConcurrentLinkedQueue<EncapsulatedPacket> packetQueue = new ConcurrentLinkedQueue<>();

	protected NioEventLoopGroup group = new NioEventLoopGroup();

	public RakNetSession(RakNetServer server, SessionManager handler, InetSocketAddress address, ChannelHandlerContext context) {
		this.server = server;
		this.handler = handler;
		this.address = address;
		this.context = context;
		group.scheduleAtFixedRate(this::tick, 0, UPDATE_PERIOD, TimeUnit.MILLISECONDS);
		log.info("Session created: [{}]", address);
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

	public ConcurrentLinkedQueue<EncapsulatedPacket> getPacketQueue() {
		return packetQueue;
	}

	public void ping(PacketReliability reliability) {
		ConnectedPingPacket packet = new ConnectedPingPacket();
		packet.pingTime = this.getServer().getRakNetTimeMS();
		this.sendEncapsulatedPacket(packet, reliability, PacketPriority.IMMEDIATE, 0);
		this.lastPingTime = System.currentTimeMillis();
	}

	public void ping() {
		this.ping(PacketReliability.UNRELIABLE);
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

	@Override
	public void close(String reason) {
		if(this.closed) {
			return;
		}
		this.closed = true;
		this.disconnect();
		this.group.shutdownGracefully();
		this.getHandler().remove(this);
		this.onClosed();
		log.info("Closed session [{}] due to {}", getAddress(), reason);
	}

	@Override
	public void close() {
		this.close("Unknown");
	}

	public void disconnect() {
		this.setState(SessionState.DISCONNECTED);
		this.ackQueue.clear();
		this.packetQueue.clear();
	}

	public boolean isClosed() {
		return closed;
	}

	public boolean isActive() {
		return active;
	}

	protected void onClosed() {

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

		if(this.ackQueue.size() > 0) {
			AcknowledgePacket pk = AcknowledgePacket.createACK();
			pk.records = this.ackQueue;
			this.sendPacket(pk);
			log.info("Sending ACK packet with records: {}", pk.records);
			this.ackQueue.clear();
		}

		this.sendQueue();
	}

	public void updateReceivedTime() {
		this.lastReceivedTime = System.currentTimeMillis();
	}

	@Override
	public void handle(DataPacket packet) {
		this.updateReceivedTime();
		if(packet instanceof ConnectionRequestPacket) {
			ConnectionRequestAcceptedPacket pk = new ConnectionRequestAcceptedPacket();
			pk.address = this.getAddress();
			pk.sendPingTime = ((ConnectionRequestPacket) packet).sendPingTime;
			pk.sendPongTime = this.getServer().getRakNetTimeMS();
			this.sendEncapsulatedPacket(pk, PacketReliability.UNRELIABLE, PacketPriority.IMMEDIATE, 0);
		} else if(packet instanceof DisconnectionNotificationPacket) {
			this.close("client disconnect");
		} else if(packet instanceof ConnectedPingPacket) {
			log.info("Received ping & sending pong");
			ConnectedPongPacket pk = new ConnectedPongPacket();
			pk.sendPingTime = ((ConnectedPingPacket) packet).pingTime;
			pk.sendPongTime = this.getServer().getRakNetTimeMS();
			this.sendEncapsulatedPacket(pk);
		} else if(packet instanceof ConnectedPongPacket) {
			log.info("Received pong packet");
		}
	}

	public void handleDatagram(RakNetDatagram packet) {
		if(packet != null) {
			this.ackQueue.add(packet.sequenceNumber);
			if(this.highestSequenceNumber < packet.sequenceNumber) this.highestSequenceNumber = packet.sequenceNumber;
			for(EncapsulatedPacket encapsulated : packet.packets) {
				handleEncapsulated(encapsulated);
			}
		}
	}

	public void handleEncapsulated(EncapsulatedPacket packet) {
		this.handle(RakNetPacketFactory.from(packet.buffer.copy()));
	}

	public void handleACK(AcknowledgePacket packet) {
		log.info("Received ACK");
		log.info("ACK records: {}", packet.records);
		this.updateReceivedTime();
	}

	public void handleNAK(AcknowledgePacket packet) {
		this.updateReceivedTime();
	}

	@Override
	public void sendPacket(DataPacket packet, PacketReliability reliability, boolean immediate) {
		PacketEncoder encoder = new PacketEncoder();
		this.getContext().writeAndFlush(new DatagramPacket(packet.write(encoder), this.getAddress()));
	}

	@Override
	public void sendPacket(DataPacket packet, boolean immediate) {
		sendPacket(packet, PacketReliability.UNRELIABLE, immediate);
	}

	@Override
	public void sendPacket(DataPacket packet) {
		sendPacket(packet, false);
	}

	public void sendEncapsulatedPacket(DataPacket packet, PacketReliability reliability, PacketPriority priority, @Nonnegative int orderChannel) {
		EncapsulatedPacket encapsulated = new EncapsulatedPacket();
		encapsulated.reliability = reliability;
		encapsulated.orderChannel = orderChannel;
		encapsulated.buffer = packet.write(new PacketEncoder());
		if(priority != PacketPriority.IMMEDIATE) {
			this.packetQueue.add(encapsulated);
		} else {
			RakNetDatagram datagram = new RakNetDatagram();
			datagram.packets.add(encapsulated);
			this.sendDatagram(datagram);
		}
	}

	public void sendEncapsulatedPacket(DataPacket packet, PacketReliability reliability, PacketPriority priority) {
		sendEncapsulatedPacket(packet, reliability, priority, 0);
	}

	public void sendEncapsulatedPacket(DataPacket packet, PacketReliability reliability) {
		sendEncapsulatedPacket(packet, reliability, PacketPriority.HIGH);
	}

	public void sendEncapsulatedPacket(DataPacket packet) {
		sendEncapsulatedPacket(packet, PacketReliability.UNRELIABLE);
	}

	public void sendDatagram(RakNetDatagram datagram) {
		datagram.sequenceNumber = this.sendSequenceNumber++;
		this.getContext().writeAndFlush(new DatagramPacket(datagram.write(new PacketEncoder()), this.getAddress()));
	}

	public void sendQueue() {
		if(this.packetQueue.size() > 0) {
			RakNetDatagram datagram = new RakNetDatagram();
			datagram.packets = new ArrayList<>(this.packetQueue);
			this.packetQueue.clear();
		}
	}

}