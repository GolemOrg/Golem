package net.gollum.command.defaults;

import net.gollum.command.Command;
import net.gollum.command.CommandSender;
import net.gollum.command.Permission;

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
