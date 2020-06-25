package net.golem.network.session;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.golem.Server;
import net.golem.network.protocol.*;
import net.golem.network.protocol.packs.ResourcePackClientResponsePacket;
import net.golem.network.protocol.packs.ResourcePackStackPacket;
import net.golem.network.protocol.packs.ResourcePacksInfoPacket;
import net.golem.network.types.PlayStatus;
import net.golem.player.Player;
import net.golem.player.PlayerInfo;
import net.golem.raknet.protocol.DataPacket;

@Log4j2
@ToString
public class GameSessionAdapter implements PacketAdapter {

	private Server server;

	private Player player;

	private GameSession session;

	public GameSessionAdapter(Server server, GameSession session) {
		this.server = server;
		this.session = session;
	}

	public Player getPlayer() {
		return player;
	}

	public void disconnect(String message, String reason) {
		DisconnectPacket pk = new DisconnectPacket();
		pk.kickMessage = message;
		sendPacket(pk, true);
		close(reason);
	}

	public void disconnect(String reason) {
		disconnect(reason, reason);
	}

	public void close(String reason) {
		session.close(reason);
	}

	public void sendPacket(DataPacket packet) {
		sendPacket(packet, false);
	}

	public void sendPacket(DataPacket packet, boolean immediate) {
		session.sendPacket(packet, immediate);
	}

	@Override
	public boolean handle(ResourcePackClientResponsePacket packet) {
		switch(packet.status) {
			case REFUSED:
				this.disconnect("You must accept resource packs to join this server.", "refused to accept the resource packs");
				return true;
			case SEND_PACKS:
				//TODO: Pack support :)
				return true;
			case HAVE_ALL_PACKS:
				ResourcePackStackPacket resourcePackStackPacket = new ResourcePackStackPacket();
				//TODO: Pack support :)
				resourcePackStackPacket.forced = false;
				sendPacket(resourcePackStackPacket);
				return true;
			case COMPLETED:
				StartGamePacket startGamePacket = new StartGamePacket();
				startGamePacket.entityRuntimeId = player.getId();
				startGamePacket.entityUniqueId = player.getId();
				startGamePacket.gamemode = player.getGameMode();
				sendPacket(startGamePacket);
				return true;
		}
		return false;
	}

	@Override
	public boolean handle(ResourcePacksInfoPacket packet) {
		return true;
	}

	@Override
	public boolean handle(ResourcePackStackPacket packet) {
		return true;
	}

	@Override
	public boolean handle(DisconnectPacket packet) {
		return true;
	}

	@Override
	public boolean handle(LoginPacket packet) {
		PlayStatusPacket pk = new PlayStatusPacket();
		if(packet.protocol != Server.PROTOCOL_VERSION) {
			pk.status = packet.protocol > Server.PROTOCOL_VERSION ? PlayStatus.FAILED_SERVER : PlayStatus.FAILED_CLIENT;
			sendPacket(pk, true);
			session.close(String.format("incompatible protocol version (%s)", packet.protocol));
			return true;
		}
		pk.status = PlayStatus.LOGIN_SUCCESS;
		sendPacket(pk);
		PlayerInfo info = new PlayerInfo(packet.username, packet.clientUUID);
		Player player = server.getPlayerManager().createPlayer(this, info);
		if(player == null) {
			log.error("An exception occurred while creating the player");

			return true;
		}
		this.player = player;
		//TODO: Implement pack support
		ResourcePacksInfoPacket packsInfoPacket = new ResourcePacksInfoPacket();
		sendPacket(packsInfoPacket);
		return true;
	}

	@Override
	public boolean handle(PlayStatusPacket packet) {
		return true;
	}

	@Override
	public boolean handle(StartGamePacket packet) {
		return true;
	}

	@Override
	public boolean handle(GamePacket packet) {
		if(packet != null) {
			if(!packet.handle(this)) {
				log.info("Unhandled packet: {}", packet);
			}
			return true;
		}
		return false;
	}

}
