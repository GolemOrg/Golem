package net.golem.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.protocol.GamePacketFactory;
import net.golem.network.protocol.PacketBatch;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.RakNetServer;
import net.golem.raknet.protocol.RawPacket;
import net.golem.raknet.session.RakNetSession;
import net.golem.raknet.session.SessionManager;

import java.net.InetSocketAddress;

@Log4j2
public class GameSession extends RakNetSession {

	public GameSession(RakNetServer server, SessionManager handler, ChannelHandlerContext context, InetSocketAddress address) {
		super(server, handler, context, address);
		log.info("Session created: [{}]", address);
	}

	@Override
	public void close(String reason) {
		super.close(reason);
		log.info("Closing session [{}] due to {}", getAddress(), reason);
	}

	@Override
	public void handle(DataPacket packet) {
		ByteBuf buffer = ((RawPacket) packet).buffer.copy();
		DataPacket pk = GamePacketFactory.from(buffer);
		log.info("Packet: {}", pk);
		if(pk != null) {
			if(pk instanceof PacketBatch) {
				handlePacketBatch((PacketBatch) pk);
			} else {
				handleGamePacket(pk);
			}
		}
	}

	public void handlePacketBatch(PacketBatch packetBatch) {

	}

	public void handleGamePacket(DataPacket packet) {
	}
}
