package net.golem.network.raknet.protocol;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

import java.util.*;

@Log4j2
public class AcknowledgePacket extends SessionPacket {

	public static final int RECORD_TYPE_RANGE = 0;
	public static final int RECORD_TYPE_SINGLE = 1;

	public Set<Integer> records = new HashSet<>();

	public AcknowledgePacket(int id) {
		super(id);
	}

	public static AcknowledgePacket createACK() {
		return new AcknowledgePacket(RakNetPacketIds.ACK);
	}

	public static AcknowledgePacket createNAK() {
		return new AcknowledgePacket(RakNetPacketIds.NAK);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		records = new HashSet<>();
		int count = decoder.readUnsignedShort();
		for(int i = 0; i < count; i++) {
			int type = decoder.readByte();
			if(type == RECORD_TYPE_SINGLE) {
				records.add(decoder.readUnsignedMediumLE());
			} else {
				int startIndex = decoder.readUnsignedMediumLE();
				int endIndex = decoder.readUnsignedMediumLE();
				for(int index = startIndex; index <= endIndex; index++) {
					records.add(index);
				}
			}
		}
	}

	@Override
	public void encode(PacketEncoder encoder) {
		ByteBuf buffer = Unpooled.buffer();
		try {
			int recordCount = 0;
			if (records.size() > 0) {
				PeekingIterator<Integer> iterator = Iterators.peekingIterator(records.iterator());
				while (iterator.hasNext()) {
					int current = iterator.next();
					boolean single = !iterator.hasNext() || (iterator.peek() - current == 1);
					buffer.writeByte(single ? RECORD_TYPE_SINGLE : RECORD_TYPE_RANGE);
					buffer.writeMediumLE(current);
					if (!single) buffer.writeMediumLE(iterator.next());
					++recordCount;
				}
			}
			encoder.writeShort((short) recordCount);
			encoder.writeBytes(buffer);
		} finally {
			buffer.release();
		}
	}

	public boolean isACK() {
		return this.id == RakNetPacketIds.ACK;
	}

	public boolean isNAK() {
		return this.id == RakNetPacketIds.NAK;
	}

	@Override
	public String toString() {
		return "AcknowledgePacket{" +
				"records=" + records +
				'}';
	}
}