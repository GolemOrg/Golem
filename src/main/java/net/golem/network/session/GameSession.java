package net.golem.network.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.Server;
import net.golem.network.protocol.*;
import net.golem.raknet.enums.PacketReliability;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.RakNetServer;
import net.golem.raknet.protocol.RawPacket;
import net.golem.raknet.session.RakNetSession;
import net.golem.raknet.session.SessionManager;

import java.net.InetSocketAddress;
import java.util.ArrayList;

@Log4j2
public class GameSession extends RakNetSession {

	private Server server;

	public GameSession(RakNetServer rakNetServer, SessionManager handler, ChannelHandlerContext context, InetSocketAddress address) {
		super(rakNetServer, handler, context, address);
		log.debug("Session created: [{}]", address);
		server = Server.getInstance();
		addListener(new GameSessionListener(server, this));
	}

	@Override
	public void close(String reason) {
		super.close(reason);
		log.debug("Closing session [{}] due to {}", getAddress(), reason);
	}

	@Override
	public void handle(DataPacket packet) {
		if(packet instanceof RawPacket) {
			DataPacket pk = GamePacketFactory.readRaw((RawPacket) packet);
			if(pk instanceof PacketBatch) {
				handlePacketBatch((PacketBatch) pk);
			}
		}
	}

	public void handlePacketBatch(PacketBatch batch) {
		batch.packets.forEach(this::handlePacket);
	}

	public void handlePacket(DataPacket packet) {
		if(packet instanceof RawPacket) return;
		getListeners().forEach(listener -> listener.onPacket(packet));
	}

	public void sendPacketBatch(PacketBatch batch, boolean immediate) {
		getEncodeLayer().sendEncapsulatedPacket(batch, PacketReliability.RELIABLE_ORDERED, 0, immediate);
	}

	public void sendPacket(DataPacket packet, boolean immediate) {
		PacketBatch batch = new PacketBatch();
		batch.packets.add(packet);
		sendPacketBatch(batch, immediate);
	}

	public void sendPackets(ArrayList<DataPacket> packets, boolean immediate) {
		PacketBatch batch = new PacketBatch();
		batch.packets = packets;
		sendPacketBatch(batch, immediate);
	}
}
