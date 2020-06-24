package net.golem.player;

import lombok.extern.log4j.Log4j2;
import net.golem.Server;
import net.golem.network.session.GameSession;
import net.golem.network.session.GameSessionAdapter;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
public class PlayerManager {

	private Server server;

	private HashMap<UUID, Player> players = new HashMap<>();

	private Class<? extends Player> playerInterface = Player.class;

	public PlayerManager(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	public HashMap<UUID, Player> getPlayers() {
		return players;
	}

	public Class<? extends Player> getPlayerInterface() {
		return playerInterface;
	}

	public void setPlayerInterface(Class<? extends Player> playerInterface) {
		this.playerInterface = playerInterface;
	}

	public Collection<Player> getOnlinePlayers() {
		return players.values()
				.stream()
				.filter(Player::isLoggedIn)
				.collect(Collectors.toList());
	}

	public Player createPlayer(GameSessionAdapter sessionAdapter, PlayerInfo info) {
		try {
			Player player = playerInterface.getConstructor(GameSessionAdapter.class, PlayerInfo.class).newInstance(sessionAdapter, info);
			addPlayer(player);
			return player;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addPlayer(Player player) {
		players.put(player.getInfo().getUUID(), player);
	}

	public void removePlayer(Player player) {
		players.remove(player.getInfo().getUUID());
	}
}
