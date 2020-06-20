package net.golem.network;

import net.golem.Server;
import net.golem.raknet.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerIdentifier extends Identifier {

	private final Server server;

	public ServerIdentifier(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public ArrayList<Object> getValues() {
		return new ArrayList<>(Arrays.asList(
				getServer().getConfiguration().getName(),
				Server.PROTOCOL_VERSION,
				getServer().getConfiguration().getVersion(),
				getServer().getPlayerManager().getOnlinePlayers().size(),
				getServer().getConfiguration().getMaxPlayerCount(),
				getServer().getGlobalUniqueId().getMostSignificantBits(),
				getServer().getConfiguration().getWorldName(),
				getServer().getConfiguration().getDefaultGamemode(),
				getServer().getConfiguration().isLimitedToSwitch() ? 0 : 1,
				getServer().getConfiguration().getPort(),
				getServer().getConfiguration().getIPv6Port()
			)
		);
	}
}
