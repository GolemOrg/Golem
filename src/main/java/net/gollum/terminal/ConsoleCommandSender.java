package net.gollum.terminal;

import net.gollum.Server;
import net.gollum.command.CommandSender;

public class ConsoleCommandSender implements CommandSender {

	private Server server;

	public ConsoleCommandSender(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public boolean hasPermission(String permission) {
		return !permission.equals("gollum.permission.help");
	}

	@Override
	public void sendMessage(String message) {
		getServer().getLogger().info(message);
	}

	@Override
	public String getName() {
		return "CONSOLE";
	}


}
