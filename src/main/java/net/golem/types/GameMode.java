package net.golem.types;

import java.util.Arrays;

public enum GameMode {
	SURVIVAL("Survival"),
	CREATIVE( "Creative"),
	ADVENTURE( "Adventure"),
	SPECTATOR("Spectator");
	
	private final String name;

	GameMode(String name){
		this.name = name;
	}
	
	public int getId() {
		return this.ordinal();
	}

	public String getName() {
		return this.name;
	}

	public String translate() {
		return "%gameMode." + getName().toLowerCase();
	}

	public static GameMode create(int id) throws Exception {
		return Arrays.stream(GameMode.values())
				.filter(gameMode -> gameMode.ordinal() == (id & 0x03))
				.findFirst()
				.orElseThrow(() -> new Exception(String.format("Invalid GameMode %d", id)));
	}
}
