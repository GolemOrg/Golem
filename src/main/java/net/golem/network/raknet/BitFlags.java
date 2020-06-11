package net.golem.network.raknet;

public enum BitFlags {

	VALID(0x80),
	ACK(0x40),
	NAK(0x20),

	PACKET_PAIR(0x10),
	CONTINUOUS_SEND(0x08),
	NEEDS_B_AND_AS(0x04);

	private byte id;

	BitFlags(int id) {
		this.id = (byte) id;
	}

	public byte getId() {
		return id;
	}
}