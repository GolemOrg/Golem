package net.golem.network.raknet.handler.packet;

import io.netty.channel.ChannelHandlerContext;
import net.golem.Server;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.identifier.Identifier;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPingPacket;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPongPacket;

public class UnconnectedPingPacketHandler extends RakNetInboundPacketHandler<UnconnectedPingPacket> {

	public UnconnectedPingPacketHandler(RakNetServer rakNet) {
		super(rakNet, UnconnectedPingPacket.class);
	}

	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<UnconnectedPingPacket> message) {
		UnconnectedPingPacket ping = message.content();
		UnconnectedPongPacket pong = new UnconnectedPongPacket();
		Server server = Server.getInstance();
		Identifier identifier = server.getIdentifier();
		pong.pingId = ping.pingId;
		pong.guid = server.getConfiguration().getGlobalUniqueId().getMostSignificantBits();
		pong.serverName = identifier.build();
		this.sendPacket(context, pong, message.recipient());
	}
}
