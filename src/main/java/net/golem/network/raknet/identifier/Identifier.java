package net.golem.network.raknet.identifier;

import net.golem.Server;

import java.util.ArrayList;
import java.util.Arrays;

public class Identifier {

	private static final String HEADER = "MCPE";

	private static final String SEPARATOR = ";";

	private Server server;

	public Identifier(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
	public ArrayList<Object> getValues() {
		return new ArrayList<>(Arrays.asList(
				this.getServer().getConfiguration().getName(),
				Server.PROTOCOL_VERSION,
				this.getServer().getConfiguration().getVersion(),
				this.getServer().getPlayerManager().getOnlinePlayers().size(),
				this.getServer().getConfiguration().getMaxPlayerCount(),
				this.getServer().getGlobalUniqueId().getMostSignificantBits(),
				this.getServer().getConfiguration().getWorldName(),
				this.getServer().getConfiguration().getDefaultGamemode(),
				this.getServer().getConfiguration().isLimitedToSwitch() ? "0" : "1",
				this.getServer().getConfiguration().getPort(),
				this.getServer().getConfiguration().getPort()
			)
		);
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
