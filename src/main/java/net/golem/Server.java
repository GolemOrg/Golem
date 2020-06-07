package net.golem;


import lombok.extern.log4j.Log4j2;
import net.golem.block.BlockFactory;
import net.golem.command.CommandRegistry;
import net.golem.item.ItemFactory;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.identifier.Identifier;
import net.golem.player.PlayerManager;
import net.golem.terminal.ServerConsole;
import net.golem.world.WorldManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;


@Log4j2
public class Server {

	/**
	 * TODO: Move this stuff
	 */
	public static final int PROTOCOL_VERSION = 390;
	public static final String SERVER_VERSION = "1.14.60";


	protected static int TICKS_PER_SECOND = 20;

	private static final int SLEEP_TIME = 1000 / TICKS_PER_SECOND;

	protected double tickCounter = 0;

	protected long nextTick;

	private static Server instance;

	private Identifier identifier;

	private ServerConfiguration configuration;

	private ServerConsole console;

	private CommandRegistry commandRegistry;

	private BlockFactory blockFactory;
	private ItemFactory itemFactory;

	private PlayerManager playerManager;
	private WorldManager worldManager;

	private AtomicBoolean running = new AtomicBoolean(true);

	public Server() {
		this.console = new ServerConsole(this);
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
		this.configuration = new ServerConfiguration("Golem", 100, "World", "Survival");
		// initiate the registries & factories
		this.commandRegistry = new CommandRegistry(this);
		this.blockFactory = new BlockFactory(this);
		this.itemFactory = new ItemFactory(this);

		this.playerManager = new PlayerManager(this);
		this.worldManager = new WorldManager(this);
		this.identifier = new Identifier(this);
		try {
			new RakNetServer(19132);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getLogger().info("The server has started successfully!");
		this.tickProcessor();
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void stop() {
		getLogger().info("Shutting down server...");
		this.setRunning(false);
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
		this.nextTick = System.currentTimeMillis();
		while(this.isRunning()) {
			try {
				this.tick();
				this.sleep();
			} catch (RuntimeException exception) {
				exception.printStackTrace();
			}
		}
	}

}
