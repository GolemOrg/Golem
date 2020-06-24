package net.golem.network;

import net.golem.Server;
import net.golem.network.protocol.GamePacketFactory;
import net.golem.network.session.GameSession;
import net.golem.raknet.RakNetServer;

public class NetworkLayer {

	private RakNetServer rakNetServer;

	private static int compressionLevel;

	public NetworkLayer(Server server, int compressionLevel) {
		rakNetServer = new RakNetServer("0.0.0.0", server.getConfiguration().getPort(), new ServerListener(), server.getIdentifier());
		initialize();
		NetworkLayer.compressionLevel = compressionLevel;
	}

	public static int getCompressionLevel() {
		return compressionLevel;
	}

	public void initialize() {
		GamePacketFactory.register();
		rakNetServer.getSessionManager().setSessionInterface(GameSession.class);
	}

	public RakNetServer getRakNetServer() {
		return rakNetServer;
	}
}
