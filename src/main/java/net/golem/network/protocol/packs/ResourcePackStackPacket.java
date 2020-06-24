package net.golem.network.protocol.packs;

import net.golem.Server;
import net.golem.network.GamePacketIds;
import net.golem.network.protocol.GamePacket;
import net.golem.network.session.GameSessionAdapter;
import net.golem.packs.ResourcePack;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;

import java.util.ArrayList;

public class ResourcePackStackPacket extends GamePacket {

	public boolean forced = false;

	public boolean isExperimental = false;
	public String baseGameVersion = Server.NETWORK_VERSION;

	public ArrayList<ResourcePack> resourcePacks = new ArrayList<>();
	public ArrayList<ResourcePack> behaviorPacks = new ArrayList<>();


	public ResourcePackStackPacket() {
		super(GamePacketIds.RESOURCE_PACKS_STACK_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeBoolean(forced);
		encoder.writeUnsignedVarInt(resourcePacks.size());
		resourcePacks.forEach(resourcePack -> encodePack(encoder, resourcePack));
		encoder.writeUnsignedVarInt(behaviorPacks.size());
		behaviorPacks.forEach(behaviorPack -> encodePack(encoder, behaviorPack));
		encoder.writeBoolean(isExperimental);
		encoder.writeString(baseGameVersion);
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}

	public void encodePack(PacketEncoder encoder, ResourcePack pack) {
		encoder.writeString(pack.getId());
		encoder.writeString(pack.getVersion());
		encoder.writeString(""); // subpack name
	}

	@Override
	public boolean handle(GameSessionAdapter adapter) {
		return false;
	}
}
