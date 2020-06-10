package net.golem.network.raknet.protocol;

import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

public class DisconnectionNotificationPacket extends DataPacket {

	public DisconnectionNotificationPacket() {
		super(RakNetPacketIds.DISCONNECTION_REQUEST);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		// no payload
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}
}
