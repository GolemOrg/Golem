package net.golem.network.raknet.protocol.connection;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class ConnectionRequestPacket extends RakNetPacket {

	public int clientId;

	public int sendPingTime;

	public boolean useSecurity = false;

	public ConnectionRequestPacket() {
		super(RakNetPacketIds.CONNECTION_REQUEST);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		clientId = (int) decoder.readLong();
		sendPingTime = (int) decoder.readLong();
		useSecurity = decoder.readBoolean();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeLong(clientId);
		encoder.writeLong(sendPingTime);
		encoder.writeBoolean(useSecurity);
	}
}
