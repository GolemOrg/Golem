package net.golem.network.raknet.protocol.connection.connected;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class ConnectedPingPacket extends RakNetPacket {

	public long pingTime;

	public ConnectedPingPacket() {
		super(RakNetPacketIds.CONNECTED_PING);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		this.pingTime = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeLong(this.pingTime);
	}
}
