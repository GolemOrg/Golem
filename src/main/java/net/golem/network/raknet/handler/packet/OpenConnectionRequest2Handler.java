package net.golem.network.raknet.handler.packet;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.connection.reply.OpenConnectionReply2Packet;
import net.golem.network.raknet.protocol.connection.request.OpenConnectionRequest2Packet;
import net.golem.network.raknet.session.SessionException;

@Log4j2
public class OpenConnectionRequest2Handler extends RakNetInboundPacketHandler<OpenConnectionRequest2Packet> {

	public OpenConnectionRequest2Handler(RakNetServer rakNet) {
		super(rakNet, OpenConnectionRequest2Packet.class);
	}


	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<OpenConnectionRequest2Packet> message) {
		OpenConnectionRequest2Packet request = message.content();
		OpenConnectionReply2Packet response = new OpenConnectionReply2Packet();

		response.serverId = getRakNet().getGlobalUniqueId().getMostSignificantBits();
		response.maximumTransferUnits = request.maximumTransferUnits;

		response.clientAddress = message.recipient();

		this.sendPacket(context, response, message.recipient());
		try {
			this.getRakNet().getSessionManager().create(message.recipient());
		} catch (SessionException e) {
			e.printStackTrace();
		}
	}
}