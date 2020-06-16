package net.golem.network.raknet.handler.packet;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.connection.IncompatibleProtocolPacket;
import net.golem.network.raknet.protocol.connection.request.OpenConnectionRequest1Packet;
import net.golem.network.raknet.protocol.connection.reply.OpenConnectionReply1Packet;


@Log4j2
public class OpenConnectionRequest1Handler extends RakNetInboundPacketHandler<OpenConnectionRequest1Packet> {

	public OpenConnectionRequest1Handler(RakNetServer rakNet) {
		super(rakNet, OpenConnectionRequest1Packet.class);
	}

	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<OpenConnectionRequest1Packet> message) {
		OpenConnectionRequest1Packet request = message.content();
		if(request.networkProtocol != RakNetServer.PROTOCOL_VERSION) {
			IncompatibleProtocolPacket incompatible = new IncompatibleProtocolPacket();
			incompatible.guid = getRakNet().getGlobalUniqueId().getMostSignificantBits();
			incompatible.protocolVersion = RakNetServer.PROTOCOL_VERSION;
			this.sendPacket(context, incompatible, message.recipient());
			log.info(String.format("Connection refused from [%s]: Incompatible RakNet protocol version: %s. Expected: %s", message.recipient(), request.networkProtocol, RakNetServer.PROTOCOL_VERSION));
			return;
		}
		OpenConnectionReply1Packet response = new OpenConnectionReply1Packet();
		response.maximumTransferUnits = request.maximumTransferUnits;
		response.guid = getRakNet().getGlobalUniqueId().getMostSignificantBits();
		this.sendPacket(context, response, message.recipient());
	}
}