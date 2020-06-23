package net.golem.network.protocol;

import net.golem.network.GamePacketIds;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.raknet.protocol.DataPacket;

public class StartGamePacket extends DataPacket {

	public StartGamePacket() {
		super(GamePacketIds.START_GAME_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {

	}

	@Override
	public void decode(PacketDecoder decoder) {

	}
}
