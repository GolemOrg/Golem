package net.golem.network.raknet.protocol.connection.connected;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class ConnectedPongPacket extends RakNetPacket {

	public long sendPingTime;

	public long sendPongTime;

	public ConnectedPongPacket() {
		super(RakNetPacketIds.CONNECTED_PONG);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		sendPingTime = decoder.readLong();
		sendPongTime = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeLong(sendPingTime);
		encoder.writeLong(sendPongTime);
	}
}
