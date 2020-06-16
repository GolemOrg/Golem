package net.golem.network.raknet.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.net.InetSocketAddress;

public abstract class RakNetInboundPacketHandler<I extends DataPacket> extends SimpleChannelInboundHandler<RakNetAddressedEnvelope<I>> {

	private RakNetServer rakNet;

	private Class<? extends RakNetPacket> packetClass;

	public RakNetInboundPacketHandler(RakNetServer rakNet, Class<? extends RakNetPacket> packetClass) {
		this.rakNet = rakNet;
		this.packetClass = packetClass;
	}

	public RakNetServer getRakNet() {
		return rakNet;
	}

	@Override
	public boolean acceptInboundMessage(Object msg) {
		try {
			if(super.acceptInboundMessage(msg) && msg instanceof RakNetAddressedEnvelope) {
				//noinspection rawtypes
				return this.packetClass.isAssignableFrom(((RakNetAddressedEnvelope) msg).content().getClass());
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext context, RakNetAddressedEnvelope<I> message) {
		handlePacket(context, message);
		message.release();
	}

	protected abstract void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<I> message);

	protected void sendPacket(ChannelHandlerContext context, RakNetPacket packet, InetSocketAddress recipient) {
		PacketEncoder encoder = new PacketEncoder();
		context.writeAndFlush(new DatagramPacket(packet.write(encoder), recipient));
	}
}
