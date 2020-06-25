package net.golem.network.protocol;

import lombok.ToString;
import net.golem.network.GamePacketIds;
import net.golem.network.session.GameSessionAdapter;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;

@ToString
public class DisconnectPacket extends GamePacket {

	public boolean hideDisconnectScreen = false;

	public String kickMessage;

	public DisconnectPacket() {
		super(GamePacketIds.DISCONNECT_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeBoolean(hideDisconnectScreen);
		encoder.writeString(kickMessage);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		hideDisconnectScreen = decoder.readBoolean();
		kickMessage = decoder.readString();
	}

	@Override
	public boolean handle(GameSessionAdapter adapter) {
		return adapter.handle(this);
	}
}
