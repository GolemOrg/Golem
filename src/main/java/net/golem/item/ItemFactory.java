package net.golem.item;

import net.golem.Server;

import java.util.HashMap;

public class ItemFactory {

	private Server server;

	private HashMap<Integer, Item> items = new HashMap<>();

	public ItemFactory(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

}
