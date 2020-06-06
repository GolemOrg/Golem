package net.gollum.command;

import net.gollum.Server;

public interface CommandSender {

	boolean hasPermission(String permission);

	void sendMessage(String message);

	String getName();

	Server getServer();
}
