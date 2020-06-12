package net.golem.network.raknet.protocol;

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

		Integer[] sorted = records.toArray(new Integer[0]);
		Arrays.sort(sorted);

		int records = 0;
		int count = sorted.length;

		if(count > 0) {
			int pointer = 1;
			int start = sorted[0];
			int end = start;
			while(pointer < count) {
				int current = sorted[pointer++];
				int difference = current - end;
				if(difference == 1) {
					end = current;
				} else if(difference > 1) {
					boolean single = start == end;
					buffer.writeByte(single ? RECORD_TYPE_SINGLE : RECORD_TYPE_RANGE);
					buffer.writeMediumLE(start);
					if(!single) buffer.writeMediumLE(end);
					start = end = current;
					++records;
				}
			}

			boolean single = start == end;
			buffer.writeByte(single ? RECORD_TYPE_SINGLE : RECORD_TYPE_RANGE);
			buffer.writeMediumLE(start);
			if(!single) buffer.writeMediumLE(end);
			++records;
		}
		encoder.writeShort((short) records);
		encoder.writeBytes(buffer);
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