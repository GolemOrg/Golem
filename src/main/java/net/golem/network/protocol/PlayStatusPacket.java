package net.golem.network.protocol;

import net.golem.network.GamePacketIds;
import net.golem.network.types.PlayStatus;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.raknet.protocol.DataPacket;

public class PlayStatusPacket extends DataPacket {

	public PlayStatus status;

	public PlayStatusPacket() {
		super(GamePacketIds.PLAY_STATUS_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeInt(status.ordinal());
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}
}
