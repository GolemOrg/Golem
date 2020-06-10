package net.golem.network.raknet.protocol;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.types.PacketReliability;

import java.util.Arrays;

public class EncapsulatedPacket {

	private static final int RELIABILITY_SHIFT = 5;
	private static final int RELIABILITY_FLAGS = 0b111 << RELIABILITY_SHIFT;

	private static final int SPLIT_FLAG = 0b00010000;

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

	public byte[] buffer;

	public int identifierACK;

	public void read(PacketDecoder decoder) {
		int flags = decoder.readUnsignedByte();

		reliability = PacketReliability.from((flags & RELIABILITY_FLAGS) >> RELIABILITY_SHIFT);
		boolean hasSplit = (flags & SPLIT_FLAG) > 0;
		int length = (int) Math.ceil(decoder.readUnsignedShort() / 8d);

		if(reliability.isReliable()) {
			this.messageIndex = decoder.readMedium();
		}

		if(reliability.isSequenced()) {
			this.sequenceIndex = decoder.readMedium();
		}

		if(reliability.isSequenced() || reliability.isOrdered()) {
			this.orderIndex = decoder.readMedium();
			this.orderChannel = decoder.readByte();
		}

		if(hasSplit) {
			int splitCount = decoder.readInt();
			short splitId = decoder.readShort();
			int splitIndex = decoder.readInt();

			this.splitInfo = new SplitPacketInfo(splitId, splitIndex, splitCount);
		}

		buffer = decoder.readRemaining();
	}

	public void write(PacketEncoder encoder) {

	}


	public int getTotalLength() {
		return
				1 + // reliability
				2 + // length
				(this.reliability.isReliable() ? 3 : 0) +  // message index
				(this.reliability.isSequenced() ? 3 : 0) + // sequence index
				(this.reliability.isSequenced() || this.reliability.isOrdered() ? 3 + 1 : 0) + // order index (3) + order channel (1)
				(this.splitInfo != null ? 4 + 2 + 4 : 0) + // split count (4) + split ID (2) + split index (4)
				this.buffer.length;
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
				", buffer=" + Arrays.toString(buffer) +
				", identifierACK=" + identifierACK +
				'}';
	}
}
