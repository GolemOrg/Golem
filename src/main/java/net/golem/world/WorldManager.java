package net.golem.world;

import net.golem.Server;

import java.util.HashMap;

public class WorldManager {

	private Server server;

	private HashMap<String, World> worlds = new HashMap<>();

	public WorldManager(Server server) {
		this.server = server;
		this.load();
	}

	private void load() {

	}


	public Server getServer() {
		return server;
	}

	public HashMap<String, World> getWorlds() {
		return worlds;
	}
}
