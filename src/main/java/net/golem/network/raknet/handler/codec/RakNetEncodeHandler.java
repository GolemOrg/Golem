package net.golem.network.raknet.handler.codec;

import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.net.InetSocketAddress;
import java.util.List;

@Log4j2
public class RakNetEncodeHandler extends MessageToMessageEncoder<AddressedEnvelope<RakNetPacket, InetSocketAddress>> {

	private RakNetServer rakNet;

	public RakNetEncodeHandler(RakNetServer rakNet) {
		this.rakNet = rakNet;
	}

	public RakNetServer getRakNet() {
		return rakNet;
	}

	@Override
	protected void encode(ChannelHandlerContext context, AddressedEnvelope<RakNetPacket, InetSocketAddress> message, List<Object> list) {
		RakNetPacket packet = message.content();
		PacketEncoder encoder = new PacketEncoder();
		list.add(new DatagramPacket(packet.write(encoder), message.sender()));
	}

}
