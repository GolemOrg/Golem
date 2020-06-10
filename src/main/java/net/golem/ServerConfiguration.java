package net.golem;

public class ServerConfiguration {

	private String name;

	private String version;

	private String defaultGamemode;

	private int maxPlayerCount;

	private String worldName;

	private int port;

	private boolean limitedToSwitch;

	public ServerConfiguration(String name, String version, int maxPlayerCount, String worldName, String defaultGamemode, boolean limitedToSwitch, int port) {
		this.setName(name);
		this.version = version;
		this.setMaxPlayerCount(maxPlayerCount);
		this.setWorldName(worldName);
		this.setDefaultGamemode(defaultGamemode);
		this.limitedToSwitch = limitedToSwitch;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
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

	public boolean isLimitedToSwitch() {
		return limitedToSwitch;
	}

	public int getPort() {
		return port;
	}
}
