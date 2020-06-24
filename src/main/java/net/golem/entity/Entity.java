package net.golem.entity;

import net.golem.world.Location;

public class Entity extends Location {

	private int id = 0;

	public boolean closed = false;

	public int getId() {
		return id;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
