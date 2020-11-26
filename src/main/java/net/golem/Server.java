package net.golem;


import lombok.extern.log4j.Log4j2;
import net.golem.block.BlockFactory;
import net.golem.command.CommandRegistry;
import net.golem.item.ItemFactory;
import net.golem.network.NetworkLayer;
import net.golem.network.ServerIdentifier;
import net.golem.raknet.Identifier;
import net.golem.player.PlayerManager;
import net.golem.terminal.ServerConsole;
import net.golem.world.WorldManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


@Log4j2
public class Server {

	/**
	 * TODO: Move this stuff
	 */
	public static final int PROTOCOL_VERSION = 407;

	public static final String NETWORK_VERSION = "1.16.0";

	protected static int TICKS_PER_SECOND = 20;

	private static final int SLEEP_TIME = 1000 / TICKS_PER_SECOND;

	protected double tickCounter = 0;

	protected long nextTick;

	private UUID guid;

	private static Server instance;

	private NetworkLayer network;

	private Identifier identifier;

	private ServerConfiguration configuration;

	private final ServerConsole console = new ServerConsole(this);

	private CommandRegistry commandRegistry;

	private BlockFactory blockFactory;
	private ItemFactory itemFactory;

	private PlayerManager playerManager;
	private WorldManager worldManager;

	private final AtomicBoolean running = new AtomicBoolean(true);

	public Server() {
		instance = this;
		this.start();
	}

	public static Server getInstance() {
		return instance;
	}

	public boolean isRunning() {
		return this.running.get();
	}

	public void setRunning(boolean value) {
		this.running.set(value);
	}

	public Logger getLogger() {
		return log;
	}

	public UUID getGlobalUniqueId() {
		return guid;
	}

	public ServerConfiguration getConfiguration() {
		return configuration;
	}

	private ServerConsole getConsole() {
		return this.console;
	}

	public BlockFactory getBlockFactory() {
		return blockFactory;
	}

	public ItemFactory getItemFactory() {
		return itemFactory;
	}

	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public WorldManager getWorldManager() {
		return worldManager;
	}

	private void start() {
		getLogger().info("Starting server...");
		this.configuration = new ServerConfiguration("Golem", Server.NETWORK_VERSION, 100, "World", "Survival", false, 19132);
		// initiate the registries & factories
		this.commandRegistry = new CommandRegistry(this);
		this.blockFactory = new BlockFactory(this);
		this.itemFactory = new ItemFactory(this);

		this.playerManager = new PlayerManager(this);
		this.worldManager = new WorldManager(this);
		this.identifier = new ServerIdentifier(this);
		this.network = new NetworkLayer(this, 7);
		this.guid = network.getRakNetServer().getGlobalUniqueId();
		getLogger().info("The server has started successfully!");
		this.tickProcessor();
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void stop() {
		getLogger().info("Shutting down server...");
		setRunning(false);
	}

	public void shutdown() {
		network.getRakNetServer().shutdown();
	}

	public boolean tick() {
		++this.tickCounter;
		return true;
	}

	public void sleep() {
		long current = System.currentTimeMillis();
		long time = this.nextTick - current;
		if (time >= SLEEP_TIME) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (Math.abs(this.nextTick - current) > 1000) {
			this.nextTick = current;
		} else {
			this.nextTick += SLEEP_TIME;
		}
	}

	private void tickProcessor() {
		nextTick = System.currentTimeMillis();
		while(isRunning()) {
			try {
				tick();
				sleep();
			} catch (RuntimeException exception) {
				exception.printStackTrace();
			}
		}
		shutdown();
	}

}