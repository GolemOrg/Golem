package net.golem.player;

import net.golem.network.NetworkInformation;

import java.util.UUID;

public class Player {

	private String username, displayName;

	private NetworkInformation networkInfo;

	private UUID uuid;

	private boolean connected, loggedIn = false;

	public Player(String username, NetworkInformation networkInfo, UUID uuid) {
		this.username = username;
		this.networkInfo = networkInfo;
		this.uuid = uuid;
	}

	public String getName() {
		return username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public NetworkInformation getNetworkInfo() {
		return networkInfo;
	}

	public UUID getUUID() {
		return uuid;
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
}
