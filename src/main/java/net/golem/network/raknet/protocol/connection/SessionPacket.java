package net.golem.network.raknet.protocol.connection;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;

public class SessionPacket extends RakNetPacket {

	public SessionPacket(byte id) {
		super(id);
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}

	@Override
	public void encode(PacketEncoder encoder) {

	}
}
