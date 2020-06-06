package net.golem.terminal;

import net.golem.Server;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

public class ServerConsole extends SimpleTerminalConsole {

	protected Server server;

	protected ConsoleCommandSender sender;

	protected Thread thread;

	public ServerConsole(Server server) {
		this.server = server;
		this.sender = new ConsoleCommandSender(server);
		this.thread = new ServerConsoleThread(this);
		this.thread.start();
	}

	public Server getServer() {
		return server;
	}

	public ConsoleCommandSender getSender() {
		return sender;
	}

	public Thread getThread() {
		return thread;
	}

	@Override
	protected boolean isRunning() {
		return this.getServer().isRunning();
	}

	@Override
	protected void runCommand(String command) {
		this.getServer().getCommandRegistry().runCommand(sender, command);
	}

	@Override
	protected void shutdown() {
		this.getThread().interrupt();
		this.getServer().stop();
	}
}
