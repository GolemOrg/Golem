package net.gollum.world;

import net.gollum.math.Vector3f;

public class Location extends Position {

	public float pitch, yaw, headYaw;

	public Location() {
		this(0f, 0f, 0f, null, 0f, 0f, 0f);
	}

	public Location(float x) {
		this(x, 0f, 0f, null, 0f, 0f, 0f);
	}

	public Location(float x, float y) {
		this(x, y, 0f, null, 0f, 0f, 0f);
	}

	public Location(float x, float y, float z) {
		this(x, y, z, null, 0f, 0f, 0f);
	}

	public Location(float x, float y, float z, World world) {
		this(x, y, z, world, 0f, 0f, 0f);
	}

	public Location(float x, float y, float z, World world, float pitch) {
		this(x, y, z, world, pitch, 0f, 0f);
	}

	public Location(float x, float y, float z, World world, float pitch, float yaw) {
		this(x, y, z, world, pitch, yaw, 0f);
	}

	public Location(float x, float y, float z, World world, float pitch, float yaw, float headYaw) {
		super(x, y, z, world);
		this.pitch = pitch;
		this.yaw = yaw;
		this.headYaw = headYaw;
	}

	public Location(Position position, float pitch, float yaw, float headYaw) {
		super(position.x, position.y, position.z, position.world);
		this.pitch = pitch;
		this.yaw = yaw;
		this.headYaw = headYaw;
	}

	public Location(Vector3f vector, World world, float pitch, float yaw, float headYaw) {
		super(vector.x, vector.y, vector.z, world);
		this.pitch = pitch;
		this.yaw = yaw;
		this.headYaw = headYaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getHeadYaw() {
		return headYaw;
	}
}
