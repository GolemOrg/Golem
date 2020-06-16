package net.golem.network.raknet.protocol;

import io.netty.buffer.ByteBuf;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.types.PacketReliability;


@Log4j2
public class EncapsulatedPacket {

	private static final int RELIABILITY_SHIFT = 5;
	private static final byte RELIABILITY_FLAGS = (byte) (0b111 << RELIABILITY_SHIFT);

	private static final byte SPLIT_FLAG = 0b00010000;

	/**
	 * Reliability of the packet
	 */
	public PacketReliability reliability;
	/**
	 * Message & sequence indexes
	 */
	public int messageIndex;
	public int sequenceIndex;
	/**
	 * Ordering index & channel
	 */
	public int orderIndex;
	public int orderChannel;

	public SplitPacketInfo splitInfo;

	public ByteBuf buffer;

	public int identifierACK;

	public void decode(PacketDecoder decoder) {
		byte flags = decoder.readByte();
		reliability = PacketReliability.from((flags & RELIABILITY_FLAGS) >> RELIABILITY_SHIFT);
		boolean hasSplit = (flags & SPLIT_FLAG) != 0;
		int length = decoder.readUnsignedShort() / Byte.SIZE;


		if(reliability.isReliable()) {
			this.messageIndex = decoder.readUnsignedMediumLE();
		}

		if(reliability.isSequenced()) {
			this.sequenceIndex = decoder.readUnsignedMediumLE();
		}

		if(reliability.isSequenced() || reliability.isOrdered()) {
			this.orderIndex = decoder.readUnsignedMediumLE();
			this.orderChannel = decoder.readUnsignedByte();
		}

		if(hasSplit) {
			int splitCount = decoder.readInt();
			int splitId = decoder.readUnsignedShort();
			int splitIndex = decoder.readInt();

			this.splitInfo = new SplitPacketInfo(splitId, splitIndex, splitCount);
		}

		buffer = decoder.readSlice(length);
	}

	public void encode(PacketEncoder encoder) {
		encoder.writeByte((byte) (reliability.getId() << 5));
		encoder.writeShort((short) (buffer.writerIndex() << 3));

		if(reliability.isReliable()) {
			encoder.writeMediumLE(messageIndex);
		}

		if(reliability.isSequenced()) {
			encoder.writeMediumLE(sequenceIndex);
		}

		if(reliability.isOrdered()) {
			encoder.writeMediumLE(orderIndex);
			encoder.writeByte((byte) orderChannel);
		}

		if(splitInfo != null) {
			encoder.writeInt(splitInfo.getSplitCount());
			encoder.writeShort((short) splitInfo.getSplitId());
			encoder.writeInt(splitInfo.getSplitIndex());
		}
		encoder.getBuffer().writeBytes(buffer);
	}


	public int getTotalLength() {
		return
				1 + // reliability
				2 + // length
				(this.reliability.isReliable() ? 3 : 0) +  // message index
				(this.reliability.isSequenced() ? 3 : 0) + // sequence index
				(this.reliability.isSequenced() || this.reliability.isOrdered() ? 3 + 1 : 0) + // order index (3) + order channel (1)
				(this.splitInfo != null ? 4 + 2 + 4 : 0) + // split count (4) + split ID (2) + split index (4)
				this.buffer.writerIndex();
	}

	@Override
	public String toString() {
		return "EncapsulatedPacket{" +
				"reliability=" + reliability +
				", messageIndex=" + messageIndex +
				", sequenceIndex=" + sequenceIndex +
				", orderIndex=" + orderIndex +
				", orderChannel=" + orderChannel +
				", splitInfo=" + splitInfo +
				", buffer=" + (buffer.hasArray() ? buffer.array() : buffer) +
				", identifierACK=" + identifierACK +
				'}';
	}
}