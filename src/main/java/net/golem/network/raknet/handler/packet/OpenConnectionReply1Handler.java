package net.golem.network.raknet.handler.packet;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.Server;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.connection.request.OpenConnectionRequest1Packet;
import net.golem.network.raknet.protocol.connection.response.OpenConnectionReply1Packet;


@Log4j2
public class OpenConnectionReply1Handler extends RakNetInboundPacketHandler<OpenConnectionRequest1Packet> {

	public OpenConnectionReply1Handler(RakNetServer rakNet) {
		super(rakNet, OpenConnectionRequest1Packet.class);
	}

	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<OpenConnectionRequest1Packet> message) {
		OpenConnectionRequest1Packet request = message.content();
		OpenConnectionReply1Packet response = new OpenConnectionReply1Packet();
		response.maximumTransferUnits = request.maximumTransferUnits;
		response.guid = Server.getInstance().getGlobalUniqueId().getMostSignificantBits();
		this.sendPacket(context, response, message.recipient());
	}
}
