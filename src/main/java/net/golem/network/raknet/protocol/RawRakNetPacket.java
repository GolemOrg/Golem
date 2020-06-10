package net.golem.network.raknet.protocol;

import io.netty.buffer.ByteBuf;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

public class RawRakNetPacket extends RakNetPacket {

	public ByteBuf buffer;

	public RawRakNetPacket(int id) {
		super(id);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		try {
			this.buffer = decoder.readRemaining();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeBytes(buffer);
	}
}
