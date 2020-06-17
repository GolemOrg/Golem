package net.golem.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.protocol.GamePacketFactory;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.protocol.RawRakNetPacket;
import net.golem.network.raknet.session.RakNetSession;
import net.golem.network.raknet.session.SessionManager;

import java.net.InetSocketAddress;

@Log4j2
public class GameSession extends RakNetSession {

	public GameSession(RakNetServer server, SessionManager handler, InetSocketAddress address, ChannelHandlerContext context) {
		super(server, handler, address, context);
	}

	@Override
	public void handle(DataPacket packet) {
		if(!(packet instanceof RawRakNetPacket)) {
			super.handle(packet);
			return;
		}
		ByteBuf buffer = ((RawRakNetPacket) packet).buffer.copy();
		DataPacket pk = GamePacketFactory.from(buffer);
		if(pk != null) {
			this.handleGamePacket(pk);
		}
	}

	public void handleGamePacket(DataPacket packet) {

	}
}
