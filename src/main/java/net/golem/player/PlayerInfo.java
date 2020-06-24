package net.golem.player;

import lombok.ToString;

import java.util.UUID;

@ToString
public class PlayerInfo {

	private String username;

	private UUID uuid;


	public PlayerInfo(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public UUID getUUID() {
		return uuid;
	}
}
