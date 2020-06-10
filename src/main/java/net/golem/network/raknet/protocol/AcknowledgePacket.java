package net.golem.network.raknet.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AcknowledgePacket extends SessionPacket {

	public Set<Integer> records = new HashSet<>();

	public AcknowledgePacket(int id) {
		super(id);
	}

	public static AcknowledgePacket createACK() {
		return new AcknowledgePacket(RakNetPacketIds.ACK);
	}

	public static AcknowledgePacket createNACK() {
		return new AcknowledgePacket(RakNetPacketIds.NACK);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		records = new HashSet<>();
		int count = decoder.readUnsignedShort();
		for(int i = 0; i < count; i++) {
			boolean isRange = decoder.readBoolean();
			if(isRange) {
				records.add(decoder.readMedium());
			} else {
				int startIndex = decoder.readMedium();
				int endIndex = decoder.readMedium();
				for(int index = startIndex; index <= endIndex; index++) {
					records.add(index);
				}
			}
		}
	}

	@Override
	public void encode(PacketEncoder encoder) {
		ByteBuf buffer = Unpooled.buffer();

		int recodeCnt = 0;

		Integer[] records = this.records.toArray(new Integer[0]);
		Arrays.sort(records);

		for(int i = 0; i < records.length; i++) {
			int startIndex = records[i];
			int endIndex = startIndex;

			while(i + 1 < records.length && records[i + 1] == (endIndex + 1)) {
				endIndex++;
				i++;
			}

			if(startIndex == endIndex) {
				buffer.writeBoolean(true);
				buffer.writeMediumLE(startIndex);
			} else {
				buffer.writeBoolean(false);
				buffer.writeMediumLE(startIndex);
				buffer.writeMediumLE(endIndex);
				recodeCnt++;
			}
		}
		encoder.writeShort((short) recodeCnt);
		encoder.writeBytes(buffer.array());
	}
}
