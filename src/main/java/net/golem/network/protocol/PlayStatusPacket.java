package net.golem.network.protocol;

import lombok.ToString;
import net.golem.network.GamePacketIds;
import net.golem.network.session.GameSessionAdapter;
import net.golem.network.types.PlayStatus;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;

@ToString
public class PlayStatusPacket extends GamePacket {

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

	@Override
	public boolean handle(GameSessionAdapter adapter) {
		return adapter.handle(this);
	}
}
