package net.golem.network.raknet.identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class Identifier {

	private static final String HEADER = "MCPE";

	private static final String SEPARATOR = ";";

	/**
	 * The server's name
	 */
	private String name;
	/**
	 * The server's protocol version
	 */
	private int protocol;
	/**
	 * The version string of the server
	 */
	private String version;
	/**
	 * Current count of players on server
	 */
	private int onlineCount;
	/**
	 * Max amount of players allowed on server
	 */
	private int maxCount;
	/**
	 * The globally unique server identifier
	 */
	private long guid;
	/**
	 * The server's world
	 */
	private String worldName;
	/**
	 * The server's gamemode
	 */
	private String gamemode;

	public Identifier(String name, int protocol, String version, int onlineCount, int maxCount, long guid, String worldName, String gamemode) {
		this.name = name;
		this.protocol = protocol;
		this.version = version;
		this.onlineCount = onlineCount;
		this.maxCount = maxCount;
		this.guid = guid;
		this.worldName = worldName;
		this.gamemode = gamemode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getOnlineCount() {
		return onlineCount;
	}

	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public long getGlobalUniqueId() {
		return guid;
	}

	public void setGlobalUniqueId(long guid) {
		this.guid = guid;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getGamemode() {
		return gamemode;
	}

	public void setGamemode(String gamemode) {
		this.gamemode = gamemode;
	}

	public ArrayList<Object> getValues() {
		return new ArrayList<>(Arrays.asList(this.getName(), this.getProtocol(), this.getVersion(), this.getOnlineCount(), this.getMaxCount(), this.getGlobalUniqueId(), this.getWorldName(), this.getGamemode()));
	}

	public String build() {
		StringBuilder builder = new StringBuilder();
		builder.append(HEADER + SEPARATOR);
		ArrayList<Object> values = getValues();
		for(int i = 0; i < values.size(); i++) {
			Object value = values.get(i);
			builder.append(value != null ? value : "");
			if(i + 1 < values.size()) {
				builder.append(SEPARATOR);
			}
		}
		return builder.toString();
	}
}
