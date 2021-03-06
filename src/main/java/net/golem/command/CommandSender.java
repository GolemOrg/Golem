package net.golem.command;

import net.golem.Server;

public interface CommandSender {

	boolean hasPermission(String permission);

	void sendMessage(String message);

	String getName();

	Server getServer();
}
