package net.golem.network;

import net.golem.Server;
import net.golem.network.protocol.GamePacketFactory;
import net.golem.raknet.RakNetServer;

public class Network {

	private RakNetServer rakNetServer;

	public Network(Server server) {
		rakNetServer = new RakNetServer("0.0.0.0", server.getConfiguration().getPort(), new ServerListener(), server.getIdentifier());
		GamePacketFactory.register();
		rakNetServer.getSessionManager().setSessionInterface(GameSession.class);
	}

	public RakNetServer getRakNetServer() {
		return rakNetServer;
	}
}
