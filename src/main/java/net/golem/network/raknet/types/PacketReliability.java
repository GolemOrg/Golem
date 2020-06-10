package net.golem.network.raknet.types;

import java.util.Arrays;

public enum PacketReliability {

	UNRELIABLE(0, false, false, false, false),

	UNRELIABLE_SEQUENCED(1, false, false, true, false),

	RELIABLE(2, true, false, false, false),

	RELIABLE_ORDERED(3, true, true, false, false),

	RELIABLE_SEQUENCED(4, true, false, true, false),

	UNRELIABLE_WITH_ACK(5, false, false, false, true),

	RELIABLE_WITH_ACK(6, true, false, false, true),

	RELIABLE_ORDERED_WITH_ACK(7, true, true, false, true);

	private final byte id;
	private final boolean reliable;
	private final boolean ordered;
	private final boolean sequenced;
	private final boolean needsAck;

	PacketReliability(int id, boolean reliable, boolean ordered, boolean sequenced, boolean needsAck) {
		this.id = (byte) id; //can't create a constructor with bytes? dunno, but have to use int & cast to byte
		this.reliable = reliable;
		this.ordered = ordered;
		this.sequenced = sequenced;
		this.needsAck = needsAck;
	}

	public byte getId() {
		return id;
	}

	public boolean isReliable() {
		return reliable;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public boolean isSequenced() {
		return sequenced;
	}

	public boolean needsAck() {
		return this.needsAck;
	}

	public static PacketReliability from(int reliability) {
		return Arrays.stream(PacketReliability.values()).filter(packetReliability -> packetReliability.getId() == reliability).findFirst().orElse(null);
	}

}
