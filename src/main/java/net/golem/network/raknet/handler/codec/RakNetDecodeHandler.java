package net.golem.network.raknet.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.protocol.RakNetPacket;

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
		byte packetId = incoming.content().readByte();
		if(getRakNet().getPacketFactory().isRegistered(packetId)) {
			RakNetPacket packet = getRakNet().getPacketFactory().getPacket(packetId);
			if(packet == null) {
				log.error(String.format("Unsupported packet %s", packetId));
				return;
			}
			PacketDecoder decoder = new PacketDecoder(incoming.content());

			packet.decode(decoder);
			list.add(new RakNetAddressedEnvelope<>(packet, incoming.sender()));
		}
	}
}
