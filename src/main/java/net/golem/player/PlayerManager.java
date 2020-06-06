package net.golem.player;

import net.golem.Server;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager {

	private Server server;

	private HashMap<UUID, Player> players = new HashMap<>();

	public PlayerManager(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	public HashMap<UUID, Player> getPlayers() {
		return players;
	}

	public Collection<Player> getOnlinePlayers() {
		return players.values()
				.stream()
				.filter(Player::isLoggedIn)
				.collect(Collectors.toList());
	}

	public void addPlayer(Player player) {
		players.put(player.getUUID(), player);
	}

	public void removePlayer(Player player) {
		players.remove(player.getUUID());
	}
}
