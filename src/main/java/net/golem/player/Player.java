package net.golem.player;

import lombok.ToString;
import net.golem.entity.human.Human;
import net.golem.network.session.GameSessionAdapter;

@ToString
public class Player extends Human {


	private GameSessionAdapter sessionAdapter;

	private PlayerInfo info;

	private String displayName = "";

	private boolean connected, loggedIn = false;

	public Player(GameSessionAdapter sessionAdapter, PlayerInfo info) {
		this.sessionAdapter = sessionAdapter;
		this.info = info;
		displayName = info.getUsername();
	}

	public PlayerInfo getInfo() {
		return info;
	}

	public String getDisplayName() {
		return displayName;
	}

	public GameSessionAdapter getSessionAdapter() {
		return sessionAdapter;
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
