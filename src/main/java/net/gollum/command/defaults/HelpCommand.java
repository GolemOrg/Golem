package net.gollum.command.defaults;

import net.gollum.command.Command;
import net.gollum.command.CommandSender;

import java.util.ArrayList;

public class HelpCommand extends Command {

	public static final int MAX_COMMANDS_PER_PAGE = 10;

	public HelpCommand() {
		super("help", "Use this command to see all other commands");
	}

	@Override
	public void run(CommandSender sender, ArrayList<String> args) {

	}
}
