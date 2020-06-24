package net.golem.network.protocol.packs;

import lombok.ToString;
import net.golem.network.protocol.GamePacket;
import net.golem.network.session.GameSessionAdapter;
import net.golem.network.types.ResourcePackStatus;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;

import java.util.ArrayList;

@ToString
public class ResourcePackClientResponsePacket extends GamePacket {

	public ResourcePackStatus status;

	public ArrayList<String> packIds = new ArrayList<>();

	public ResourcePackClientResponsePacket(int packetId) {
		super(packetId);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeByte(status.ordinal());
		encoder.writeShortLE((short) packIds.size());
		packIds.forEach(encoder::writeString);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		status = ResourcePackStatus.from(decoder.readByte());
		int packCount = decoder.readShortLE();
		while(packCount-- > 0) packIds.add(decoder.readString());
	}

	@Override
	public boolean handle(GameSessionAdapter adapter) {
		return adapter.handle(this);
	}
}
