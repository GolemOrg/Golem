package net.golem.command;

public enum Permission {

	OP("gollum.permission.op"),
	STOP("gollum.permission.stop");

	String permission;

	Permission(String permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return permission;
	}

}
