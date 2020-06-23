package net.golem.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.protocol.GamePacketFactory;
import net.golem.network.protocol.LoginPacket;
import net.golem.network.protocol.PacketBatch;
import net.golem.network.protocol.PlayStatusPacket;
import net.golem.network.types.PlayStatus;
import net.golem.raknet.enums.PacketReliability;
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
		if(packet instanceof RawPacket) {
			DataPacket pk = GamePacketFactory.readRaw((RawPacket) packet);
			if(pk instanceof PacketBatch) {
				handlePacketBatch((PacketBatch) pk);
			}
		} else {
			log.info("Packet: {}", packet.getClass().getSimpleName());
		}
	}

	public void handlePacketBatch(PacketBatch batch) {
		batch.packets.forEach(this::handlePacket);
	}

	public void handlePacket(DataPacket packet) {
		if(packet instanceof LoginPacket) {
			PlayStatusPacket pk = new PlayStatusPacket();
			pk.status = PlayStatus.LOGIN_SUCCESS;
			sendPacket(pk, true);
			log.info("Sending play status");
		}
	}

	public void sendPacketBatch(PacketBatch batch) {
		getEncodeLayer().sendEncapsulatedPacket(batch, PacketReliability.UNRELIABLE, 0, true);
	}

	public void sendPacket(DataPacket packet, boolean immediate) {
		if(immediate) {
			PacketBatch batch = new PacketBatch();
			batch.packets.add(packet);
			sendPacketBatch(batch);
		} else {

		}
	}
}
