package net.golem.network.raknet.protocol.connection.response;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class OpenConnectionReply1Packet extends RakNetPacket {

	public long guid;

	public boolean useSecurity = false;

	public short maximumTransferUnits;

	public OpenConnectionReply1Packet() {
		super(RakNetPacketIds.OPEN_CONNECTION_RESPONSE_1);
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeMagic();
		encoder.writeLong(this.guid);
		encoder.writeBoolean(this.useSecurity);
		encoder.writeShort(maximumTransferUnits);
	}
}
