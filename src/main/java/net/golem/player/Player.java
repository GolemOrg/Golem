package net.golem.player;

import lombok.ToString;
import net.golem.Server;
import net.golem.entity.human.Human;
import net.golem.network.session.GameSessionAdapter;
import net.golem.types.GameMode;

@ToString
public class Player extends Human {

	private Server server;

	private GameSessionAdapter sessionAdapter;

	private GameMode gameMode = GameMode.SURVIVAL;

	private PlayerInfo info;

	private String displayName = "";

	private boolean connected, loggedIn = false;

	public Player(GameSessionAdapter sessionAdapter, PlayerInfo info) {
		this.server = Server.getInstance();
		this.sessionAdapter = sessionAdapter;
		this.info = info;
		displayName = info.getUsername();
	}

	public Server getServer() {
		return server;
	}

	public GameSessionAdapter getSessionAdapter() {
		return sessionAdapter;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public PlayerInfo getInfo() {
		return info;
	}

	public String getDisplayName() {
		return displayName;
	}


	public boolean isConnected() {
		return connected;
	}

	protected void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	protected void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isOnline() {
		return this.isConnected() && this.isLoggedIn();
	}

	public void close(String message, String reason, boolean notify) {
		if(isConnected() && !isClosed()) {
			if(notify) {
				sessionAdapter.disconnect(message, reason.length() > 0 ? reason : "closing");
			} else {
				sessionAdapter.close(reason);
			}
		}
	}
}
