package net.golem.block;

import net.golem.Server;

import java.util.HashMap;

public class BlockFactory {

	private Server server;

	private HashMap<BlockType, Block> blocks = new HashMap<>();

	public BlockFactory(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	public void register(Block block) {

	}
}
