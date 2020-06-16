package net.golem.network.raknet.protocol.unconnected;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacketIds;
import net.golem.network.raknet.protocol.RakNetPacket;

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
		encoder.writeMagic();
		encoder.writeLong(clientGuid);
	}

}
