package net.gollum.command;

import java.util.ArrayList;

public abstract class Command {

	private String name, description, usage, permission;

	private ArrayList<String> aliases;

	public Command(String name, String description) {
		this(name, description, String.format("/%s", name));
	}

	public Command(String name, String description, String usage) {
		this(name, description, usage, "");
	}

	public Command(String name, String description, String usage, String permission) {
		this(name, description, usage, permission, new ArrayList<>());
	}

	public Command(String name, String description, String usage, String permission, ArrayList<String> aliases) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.permission = permission;
		this.aliases = aliases;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean needsPermission() {
		return this.permission != null && this.permission.length() > 0;
	}

	public boolean hasPermission(CommandSender sender) {
		return sender.hasPermission(this.getPermission());
	}

	public ArrayList<String> getAliases() {
		return aliases;
	}

	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
	}

	public abstract void run(CommandSender sender, ArrayList<String> args);
}
