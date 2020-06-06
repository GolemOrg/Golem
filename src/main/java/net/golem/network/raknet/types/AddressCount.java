package net.golem.network.raknet.types;

public enum AddressCount {

	RAKNET(10),
	MINECRAFT(20);

	private int count;

	AddressCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return Integer.toString(count);
	}
}
