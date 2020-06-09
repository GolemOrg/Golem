package net.golem.network.raknet.protocol.unconnected;

import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

@Log4j2
public class UnconnectedPongPacket extends RakNetPacket {

	public long pingId;

	public long guid;

	public String serverName;

	public UnconnectedPongPacket() {
		super(RakNetPacketIds.UNCONNECTED_PONG);
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeLong(pingId);
		encoder.writeLong(guid);
		encoder.writeMagic();
		encoder.writeString(serverName);
	}

}
