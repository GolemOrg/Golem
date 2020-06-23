package net.golem.network.protocol;

import net.golem.network.GamePacketIds;
import net.golem.packs.ResourcePack;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.raknet.protocol.DataPacket;

import java.util.ArrayList;

public class ResourcePacksInfoPacket extends DataPacket {

	public boolean forced = false;
	public boolean scriptingEnabled = false;

	public ArrayList<ResourcePack> resourcePacks = new ArrayList<>();
	public ArrayList<ResourcePack> behaviorPacks = new ArrayList<>();

	public ResourcePacksInfoPacket() {
		super(GamePacketIds.RESOURCE_PACKS_INFO_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeBoolean(forced);
		encoder.writeBoolean(scriptingEnabled);
		encoder.writeShortLE((short) behaviorPacks.size());
		behaviorPacks.forEach(behaviorPack -> encodePack(encoder, behaviorPack));
		encoder.writeShortLE((short) resourcePacks.size());
		resourcePacks.forEach(resourcePack -> encodePack(encoder, resourcePack));
	}

	@Override
	public void decode(PacketDecoder decoder) {

	}

	public void encodePack(PacketEncoder encoder, ResourcePack pack) {
		encoder.writeString(pack.getId());
		encoder.writeString(pack.getVersion());
		encoder.writeLongLE(pack.getSize());
	}
}
