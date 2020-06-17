package net.golem.network.raknet.session.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.network.protocol.GamePacketFactory;
import net.golem.network.protocol.PacketBatch;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.protocol.*;
import net.golem.network.raknet.session.RakNetSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class SessionDecodeLayer implements CodecLayer {

	protected RakNetSession session;

	protected int highestSequenceNumber = -1;

	private ConcurrentHashMap<Integer, EncapsulatedPacket[]> splitPackets = new ConcurrentHashMap<>();

	protected Set<Integer> ackQueue = new HashSet<>();

	public SessionDecodeLayer(RakNetSession session) {
		this.session = session;
	}

	public RakNetSession getSession() {
		return session;
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

	public void handleBatch(PacketBatch packetBatch) {

	}

	public void handleEncapsulated(EncapsulatedPacket packet) {
		if(packet.splitInfo != null) {
			EncapsulatedPacket pk = handleSplit(packet);
			if(pk == null) {
				return;
			}
			DataPacket gamePacket = GamePacketFactory.from(pk.buffer);
			return;
		}
		ByteBuf buffer = packet.buffer.copy();
		try {
			getSession().handle(RakNetPacketFactory.from(buffer));
		} finally {
			buffer.release();
		}
	}

	public EncapsulatedPacket handleSplit(EncapsulatedPacket packet) {
		SplitPacketInfo info = packet.splitInfo;
		if(info == null) {
			log.error("Encapsulated packet does not contain split info!");
			return null;
		}

		int index = info.splitIndex;
		int count = info.splitCount;

		EncapsulatedPacket[] split = splitPackets.getOrDefault(info.splitId, new EncapsulatedPacket[count]);
		split[index] = packet;

		splitPackets.put(info.splitId, split);

		for(EncapsulatedPacket part : split) {
			if(part == null) {
				// not finished assembling the packet yet
				return null;
			}
		}


		EncapsulatedPacket pk = new EncapsulatedPacket();
		pk.buffer = Unpooled.buffer();
		pk.reliability = packet.reliability;
		pk.messageIndex = packet.messageIndex;
		pk.sequenceIndex = packet.sequenceIndex;
		pk.orderIndex = packet.orderIndex;
		pk.orderChannel = packet.orderChannel;

		Arrays.stream(split).forEach(encapsulated -> {
			try {
				pk.buffer.writeBytes(encapsulated.buffer);
			} finally {
				encapsulated.buffer.release();
			}
		});
		splitPackets.remove(index);
		return pk;
	}


	@Override
	public void update() {
		if(this.ackQueue.size() > 0) {
			AcknowledgePacket pk = AcknowledgePacket.createACK();
			pk.records = this.ackQueue;
			this.getSession().sendPacket(pk);
			this.ackQueue.clear();
		}
	}

	@Override
	public void disconnect() {

	}

	@Override
	public void close() {

	}
}
