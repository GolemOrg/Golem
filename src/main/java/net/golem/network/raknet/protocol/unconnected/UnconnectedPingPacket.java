package net.golem.network.raknet.protocol.unconnected;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class UnconnectedPingPacket extends RakNetPacket {

	public long pingId;

	public long clientGuid;

	public UnconnectedPingPacket() {
		super(RakNetPacketIds.UNCONNECTED_PING);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		pingId = decoder.readLong();
		decoder.skipBytes(MAGIC.length);
		clientGuid = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeLong(pingId);
		encoder.writeBytes(MAGIC);
		encoder.writeLong(clientGuid);
	}

}
