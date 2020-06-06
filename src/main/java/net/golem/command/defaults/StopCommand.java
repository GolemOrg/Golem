package net.golem.command.defaults;

import net.golem.command.Command;
import net.golem.command.CommandSender;
import net.golem.command.Permission;

import java.util.ArrayList;

public class StopCommand extends Command {

	public StopCommand() {
		super("stop", "Stops the server", "/stop", Permission.STOP.toString());
	}

	@Override
	public void run(CommandSender sender, ArrayList<String> args) {
		sender.getServer().stop();
	}
}
