package net.golem.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.protocol.*;
import net.golem.network.types.PlayStatus;
import net.golem.raknet.enums.PacketReliability;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.RakNetServer;
import net.golem.raknet.protocol.RawPacket;
import net.golem.raknet.session.RakNetSession;
import net.golem.raknet.session.SessionManager;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			ResourcePacksInfoPacket infoPk = new ResourcePacksInfoPacket();
			sendPackets(Stream.of(pk, infoPk).collect(Collectors.toCollection(ArrayList::new)), true);


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

	public void sendPackets(ArrayList<DataPacket> packets, boolean immediate) {
		if(immediate) {
			PacketBatch batch = new PacketBatch();
			batch.packets = packets;
			sendPacketBatch(batch);
		}
	}
}
