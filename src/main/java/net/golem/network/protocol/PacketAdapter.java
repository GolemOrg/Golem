package net.golem.network.protocol;

import net.golem.network.protocol.packs.ResourcePackClientResponsePacket;
import net.golem.network.protocol.packs.ResourcePackStackPacket;
import net.golem.network.protocol.packs.ResourcePacksInfoPacket;

public interface PacketAdapter {

	boolean handle(ResourcePackClientResponsePacket packet);

	boolean handle(ResourcePacksInfoPacket packet);

	boolean handle(ResourcePackStackPacket packet);

	boolean handle(DisconnectPacket packet);

	boolean handle(LoginPacket packet);

	boolean handle(PlayStatusPacket packet);

	boolean handle(StartGamePacket packet);

	boolean handle(GamePacket packet);
}
