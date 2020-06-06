package net.gollum.math;

public class Vector3f extends Vector2f {

	public float z;

	public Vector3f(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}

	public Vector3f(Vector2f vector) {
		super(vector.getX(), vector.getY());
		this.z = 0f;
	}

	public float getZ() {
		return z;
	}
}
