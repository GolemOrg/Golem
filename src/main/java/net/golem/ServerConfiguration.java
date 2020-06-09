package net.golem;

public class ServerConfiguration {

	private String name;

	private String defaultGamemode;

	private int maxPlayerCount;

	private String worldName;

	public ServerConfiguration(String name, int maxPlayerCount, String worldName, String defaultGamemode) {
		this.setName(name);
		this.setMaxPlayerCount(maxPlayerCount);
		this.setWorldName(worldName);
		this.setDefaultGamemode(defaultGamemode);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxPlayerCount() {
		return maxPlayerCount;
	}

	public String getDefaultGamemode() {
		return defaultGamemode;
	}

	public void setDefaultGamemode(String defaultGamemode) {
		this.defaultGamemode = defaultGamemode;
	}

	public void setMaxPlayerCount(int maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}
}
