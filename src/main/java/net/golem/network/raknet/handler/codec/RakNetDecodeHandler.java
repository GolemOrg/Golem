package net.golem.network.raknet.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.protocol.AcknowledgePacket;
import net.golem.network.raknet.protocol.RawRakNetPacket;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.protocol.RakNetDatagram;
import net.golem.network.raknet.protocol.RakNetPacketFactory;
import net.golem.network.raknet.session.RakNetSession;

import java.util.List;

@Log4j2
public class RakNetDecodeHandler extends MessageToMessageDecoder<DatagramPacket> {

	private RakNetServer rakNet;

	public RakNetDecodeHandler(RakNetServer rakNet) {
		this.rakNet = rakNet;
	}

	public RakNetServer getRakNet() {
		return rakNet;
	}

	@Override
	protected void decode(ChannelHandlerContext context, DatagramPacket incoming, List<Object> list) {
		if(this.getRakNet().needsContext()) {
			this.getRakNet().setContext(context);
		}
		DataPacket packet = RakNetPacketFactory.from(incoming.content().retain());
		if(packet instanceof RawRakNetPacket) {
			log.info(String.format("Raw packet caught: 0x%02x", packet.getId()));
			log.info((String.format("Buffer length: %s", ((RawRakNetPacket) packet).buffer.readableBytes())));
		}
		if(this.getRakNet().getSessionManager().contains(incoming.sender())) {
			RakNetSession session = this.getRakNet().getSessionManager().get(incoming.sender());
			if(packet instanceof RakNetDatagram) {
				session.getDecodeLayer().handleDatagram((RakNetDatagram) packet);
			} else if(packet instanceof AcknowledgePacket) {
				if(((AcknowledgePacket) packet).isACK()) {
					session.handleACK((AcknowledgePacket) packet);
				} else if(((AcknowledgePacket) packet).isNAK()) {
					session.handleNAK((AcknowledgePacket) packet);
				} else {
					log.error("Unknown ACK packet ID");
				}
			} else {
				session.handle(packet);
			}
		} else {
			list.add(new RakNetAddressedEnvelope<>(packet, incoming.sender()));
		}
		incoming.release();
	}
}