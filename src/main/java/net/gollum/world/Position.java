package net.gollum.world;

import net.gollum.math.Vector3f;

public class Position extends Vector3f {

	public World world;

	public Position() {
		this(0f, 0f, 0f, null);
	}

	public Position(float x) {
		this(x, 0f, 0f, null);
	}

	public Position(float x, float y) {
		this(x, y, 0f, null);
	}

	public Position(float x, float y, float z) {
		this(x, y, z, null);
	}

	public Position(float x, float y, float z, World world) {
		super(x, y, z);
		this.world = world;
	}

	public Position(Vector3f vector, World world) {
		super(vector.x, vector.y, vector.z);
		this.world = world;
	}

	public static Position create(Vector3f vector, World world) {
		return new Position(vector, world);
	}

	public World getWorld() {
		return world;
	}
}
