package net.golem.network.raknet;

public enum BitFlags {

	VALID(0x80),
	ACK(0x40),
	NACK(0x20),

	PACKET_PAIR(0x10),
	CONTINUOUS_SEND(0x08),
	NEEDS_B_AND_AS(0x04);

	private int id;

	BitFlags(int id) {
		this.id = (byte) id;
	}

	public int getId() {
		return id;
	}
}
