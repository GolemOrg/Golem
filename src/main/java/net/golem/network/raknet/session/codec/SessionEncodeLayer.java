package net.golem.network.raknet.session.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.EncapsulatedPacket;
import net.golem.network.raknet.protocol.RakNetDatagram;
import net.golem.network.raknet.session.RakNetSession;
import net.golem.network.raknet.types.PacketPriority;
import net.golem.network.raknet.types.PacketReliability;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SessionEncodeLayer implements CodecLayer {

	protected RakNetSession session;

	protected LinkedHashMap<Integer, DataPacket> needACKs = new LinkedHashMap<>();
	protected ConcurrentLinkedQueue<EncapsulatedPacket> packetQueue = new ConcurrentLinkedQueue<>();

	private int sendSequenceNumber = 0;

	public SessionEncodeLayer(RakNetSession session) {
		this.session = session;
	}

	public RakNetSession getSession() {
		return session;
	}

	@Override
	public void update() {
		this.sendQueue();
	}

	@Override
	public void disconnect() {
		needACKs.clear();
		packetQueue.clear();
	}

	@Override
	public void close() {

	}

	public void sendEncapsulatedPacket(DataPacket packet, PacketReliability reliability, PacketPriority priority, @Nonnegative int orderChannel) {
		if(session.isClosed()) {
			return;
		}
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
		sendEncapsulatedPacket(packet, reliability, PacketPriority.IMMEDIATE);
	}

	public void sendEncapsulatedPacket(DataPacket packet) {
		sendEncapsulatedPacket(packet, PacketReliability.UNRELIABLE);
	}

	public void sendDatagram(RakNetDatagram datagram) {
		datagram.sequenceNumber = this.sendSequenceNumber++;
		session.getContext().writeAndFlush(new DatagramPacket(datagram.write(new PacketEncoder()), getSession().getAddress()));
	}

	public void sendQueue() {
		if(this.packetQueue.size() > 0) {
			RakNetDatagram datagram = new RakNetDatagram();
			datagram.packets = new ArrayList<>(this.packetQueue);
			this.sendDatagram(datagram);
			this.packetQueue.clear();
		}
	}
}
