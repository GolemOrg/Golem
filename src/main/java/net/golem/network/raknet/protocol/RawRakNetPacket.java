package net.golem.network.raknet.protocol;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

public class RawRakNetPacket extends RakNetPacket {

	public byte[] buffer;

	public RawRakNetPacket(int id) {
		super(id);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		this.buffer = decoder.readRemaining();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeBytes(buffer);
	}
}
