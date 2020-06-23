package net.golem.network.protocol;

import net.golem.network.GamePacketIds;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.raknet.protocol.DataPacket;

public class DisconnectPacket extends DataPacket {

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
}
