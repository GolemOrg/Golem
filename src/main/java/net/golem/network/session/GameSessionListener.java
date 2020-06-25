package net.golem.network.session;

import lombok.extern.log4j.Log4j2;
import net.golem.Server;
import net.golem.network.protocol.GamePacket;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.protocol.RawPacket;
import net.golem.raknet.session.SessionListener;

@Log4j2
public class GameSessionListener implements SessionListener {

	private GameSession session;

	private GameSessionAdapter adapter;

	public GameSessionListener(Server server, GameSession session) {
		this.session = session;
		this.adapter = new GameSessionAdapter(server, session);
	}

	public GameSession getSession() {
		return session;
	}

	@Override
	public void onPacket(DataPacket packet) {
		if(packet instanceof RawPacket) return;
		adapter.handle((GamePacket) packet);
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onClose() {
		if(adapter.getPlayer() != null) {
			adapter.getPlayer().getServer().getPlayerManager().removePlayer(adapter.getPlayer());

		}
	}
}
