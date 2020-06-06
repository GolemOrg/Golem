package net.golem.types;

import java.util.Arrays;

public enum GameMode {
	SURVIVAL(0, "Survival"),
	CREATIVE(1, "Creative"),
	ADVENTURE(2, "Adventure"),
	SPECTATOR(3, "Spectator");
	
	private final int id;
	private final String name;

	GameMode(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String translate() {
		return "%gameMode." + getName().toLowerCase();
	}

	public static GameMode create(int id) throws Exception {
		return Arrays.stream(GameMode.values())
				.filter(gameMode -> gameMode.id == (id & 0x03))
				.findFirst()
				.orElseThrow(() -> new Exception(String.format("Invalid GameMode %d", id)));
	}
}
