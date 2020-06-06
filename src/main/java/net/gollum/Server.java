package net.gollum;


import lombok.extern.log4j.Log4j2;
import net.gollum.block.BlockFactory;
import net.gollum.command.CommandRegistry;
import net.gollum.item.ItemFactory;
import net.gollum.player.PlayerManager;
import net.gollum.terminal.ServerConsole;
import net.gollum.world.WorldManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;


@Log4j2
public class Server {

	protected static int TICKS_PER_SECOND = 20;

	private static final int SLEEP_TIME = 1000 / TICKS_PER_SECOND;

	protected double tickCounter = 0;

	protected long nextTick;

	private ServerConsole console;

	private CommandRegistry commandRegistry;

	private BlockFactory blockFactory;
	private ItemFactory itemFactory;

	private PlayerManager playerManager;
	private WorldManager worldManager;

	private AtomicBoolean running = new AtomicBoolean(true);

	public Server() {
		this.console = new ServerConsole(this);
		this.start();
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
		// initiate the registries & factories
		this.commandRegistry = new CommandRegistry(this);
		this.blockFactory = new BlockFactory(this);
		this.itemFactory = new ItemFactory(this);

		this.playerManager = new PlayerManager(this);
		this.worldManager = new WorldManager(this);

		getLogger().info("The server has started successfully!");
		this.tickProcessor();
	}

	public void stop() {
		getLogger().info("Shutting down server...");
		this.setRunning(false);
	}

	public boolean tick() {
		++this.tickCounter;
		//do stuff here
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
