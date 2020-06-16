package net.golem.network.raknet.handler.packet;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPingPacket;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPongPacket;

@Log4j2
public class UnconnectedPingHandler extends RakNetInboundPacketHandler<UnconnectedPingPacket> {

	public UnconnectedPingHandler(RakNetServer rakNet) {
		super(rakNet, UnconnectedPingPacket.class);
	}

	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<UnconnectedPingPacket> message) {
		UnconnectedPingPacket ping = message.content();
		UnconnectedPongPacket pong = new UnconnectedPongPacket();
		pong.pingId = ping.pingId;
		pong.guid = getRakNet().getGlobalUniqueId().getMostSignificantBits();
		pong.serverName = getRakNet().getIdentifier().build();

		this.sendPacket(context, pong, message.recipient());
	}
}
