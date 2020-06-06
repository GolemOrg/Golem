package net.golem.command;

import net.golem.Server;
import net.golem.command.defaults.HelpCommand;
import net.golem.command.defaults.StopCommand;
import net.golem.terminal.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandRegistry {

	private Server server;

	private HashMap<String, Command> commands = new HashMap<>();

	public CommandRegistry(Server server) {
		this.server = server;
		this.initialize();
	}

	public void initialize() {
		register(new HelpCommand());
		register(new StopCommand());
	}

	public HashMap<String, Command> getCommands() {
		return commands;
	}

	public void register(Command command) {
		register(command, false);
	}

	public void register(Command command, boolean override) {
		if(matchCommand(command.getName()) != null && !override) {
			this.getServer().getLogger().error(String.format("Command '%s' already exists!", command.getName()));
			return;
		}
		commands.put(command.getName().toLowerCase(), command);
	}

	public Command matchCommand(String name) {
		Command command = commands.get(name.toLowerCase());
		if(command != null) {
			return command;
		}
		for(Command current : getCommands().values()) {
			if(current.getAliases().size() > 0) {
				if(current.getAliases().stream().anyMatch(name::equalsIgnoreCase)) {
					return current;
				}
			}
		}
		return null;
	}

	public Server getServer() {
		return server;
	}

	private ArrayList<String> parseArguments(String argumentString) {
		ArrayList<String> output = new ArrayList<>();
		if(argumentString.length() > 0) {
			boolean inQuotes = false;
			StringBuilder argumentBuilder = new StringBuilder();
			for(int i = 0; i < argumentString.length(); i++) {
				char current = argumentString.charAt(i);
				switch(current) {
					case '\\':
						//skip character
						break;
					case '"':
						inQuotes = !inQuotes;
						break;
					case ' ':
						if(!inQuotes) {
							String argument = argumentBuilder.toString();
							if(argument.length() > 0) {
								output.add(argument);
								/** Clear the string builder */
								argumentBuilder.setLength(0);
							}
							break;
						}
					default:
						argumentBuilder.append(current);
				}
			}
			/** Create argument if there's any left */
			if(argumentBuilder.toString().length() > 0) output.add(argumentBuilder.toString());
		}
		return output;
	}

	public boolean runCommand(CommandSender sender, String commandString) {
		ArrayList<String> split = new ArrayList<>(Arrays.asList(commandString.split(" ")));
		if(split.size() > 0) {
			String name = split.remove(0);
			Command command = matchCommand(name);
			if(command != null) {
				if(!command.needsPermission() || command.hasPermission(sender)) {
					ArrayList<String> args = parseArguments(String.join(" ", split));
					command.run(sender, args);
					return true;
				}
				sender.sendMessage(String.format(TextColor.RED + "You do not have permission to use this command!", name));
			} else {
				sender.sendMessage(String.format(TextColor.RED + "Command '%s' not found! Please ensure you have permission before using the command!", name));
			}
		}
		return false;
	}
}
