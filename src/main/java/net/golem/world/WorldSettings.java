package net.golem.world;

import net.golem.math.Vector3f;
import net.golem.types.Difficulty;
import net.golem.types.Dimension;
import net.golem.types.GameMode;

public class WorldSettings {

	private String name;
	private int seed;

	private GameMode gameMode;
	private Difficulty difficulty;

	public int currentTick = 0;

	private Dimension dimension;
	/**
	 * Used for the client to know how to generate chunks
	 * 0 - old worlds
	 * 1 - infinite worlds
	 * 2 - flat worlds
	 * 3 - nether
	 * 4 - end
	 */
	private int generator = 1;

	private Vector3f spawn = new Vector3f(0, 0, 0);

	private boolean achievementsEnabled = false;

	private int rainLevel = 0;
	private int lightningLevel = 0;

	public WorldSettings(String name, int seed, GameMode gameMode, Difficulty difficulty) {
		this.name = name;
		this.seed = seed;
		this.gameMode = gameMode;
		this.difficulty = difficulty;
	}

	public String getName() {
		return name;
	}

	public int getSeed() {
		return seed;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public int getCurrentTick() {
		return currentTick;
	}

	public int getGenerator() {
		return generator;
	}

	public int getLightningLevel() {
		return lightningLevel;
	}

	public int getRainLevel() {
		return rainLevel;
	}

	public Vector3f getSpawn() {
		return spawn;
	}

	public boolean hasAchievementsEnabled() {
		return achievementsEnabled;
	}
}
