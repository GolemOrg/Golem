package net.golem.math;

public class Vector2f {

	public float x, y;


	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return this.x;
	}

	public int getFloorX() {
		return (int) Math.floor(x);
	}

	public void setX(int x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public int getFloorY() {
		return (int) Math.floor(y);
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean equals(Vector2f vector) {
		return this.x == vector.x && this.y == vector.y;
	}

}
