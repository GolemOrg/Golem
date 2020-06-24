package net.golem.network.types;

public enum ResourcePackStatus {
	NONE,
	REFUSED,
	SEND_PACKS,
	HAVE_ALL_PACKS,
	COMPLETED;

	private static final ResourcePackStatus[] values = values();

	public static ResourcePackStatus from(int id) {
		return values[id];
	}
}
