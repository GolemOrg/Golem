package net.golem.network.session;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.golem.Server;
import net.golem.network.protocol.DisconnectPacket;
import net.golem.network.protocol.GamePacket;
import net.golem.network.protocol.LoginPacket;
import net.golem.network.protocol.PlayStatusPacket;
import net.golem.network.protocol.packs.ResourcePackClientResponsePacket;
import net.golem.network.protocol.packs.ResourcePackStackPacket;
import net.golem.network.protocol.packs.ResourcePacksInfoPacket;
import net.golem.network.types.PlayStatus;
import net.golem.player.Player;
import net.golem.player.PlayerInfo;
import net.golem.raknet.protocol.DataPacket;

@Log4j2
@ToString
public class GameSessionAdapter {

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

	public boolean handle(LoginPacket packet) throws Exception {
		PlayStatusPacket pk = new PlayStatusPacket();
		if(packet.protocol != Server.PROTOCOL_VERSION) {
			pk.status = packet.protocol > Server.PROTOCOL_VERSION ? PlayStatus.FAILED_SERVER : PlayStatus.FAILED_CLIENT;
		} else {
			pk.status = PlayStatus.LOGIN_SUCCESS;
		}
		sendPacket(pk, true);
		if(pk.status != PlayStatus.LOGIN_SUCCESS) {
			session.close(String.format("incompatible protocol version (%s)", packet.protocol));
			return true;
		}
		PlayerInfo info = new PlayerInfo(packet.username, packet.clientUUID);
		Player player = server.getPlayerManager().createPlayer(this, info);
		if(player == null) {
			throw new Exception("An exception occurred while creating the player");
		}
		this.player = player;
		//TODO: Implement pack support
		sendPacket(new ResourcePacksInfoPacket());
		return true;
	}

	public boolean handle(PlayStatusPacket packet) {
		return true;
	}

	public boolean handle(ResourcePackClientResponsePacket packet) {
		switch(packet.status) {
			case REFUSED:
				this.disconnect("You must accept resource packs to join this server.", "refused to accept the resource packs");
				break;
			case SEND_PACKS:
				//TODO: Pack support :)
				break;
			case HAVE_ALL_PACKS:
				ResourcePackStackPacket pk = new ResourcePackStackPacket();
				sendPacket(pk);
				break;
			case COMPLETED:

				break;
			default:
				return false;
		}
		return true;
	}

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
