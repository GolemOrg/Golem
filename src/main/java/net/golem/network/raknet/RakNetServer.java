package net.golem.network.raknet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.handler.codec.RakNetDecodeHandler;
import net.golem.network.raknet.handler.codec.RakNetEncodeHandler;
import net.golem.network.raknet.handler.packet.UnconnectedPingPacketHandler;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketFactory;

import java.net.InetSocketAddress;

public class RakNetServer {

	private RakNetPacketFactory packetFactory = new RakNetPacketFactory(this);

	private PacketEncoder encoder = new PacketEncoder();
	private PacketDecoder decoder = new PacketDecoder();

	private Channel channel;

	private final int port;

	public RakNetServer(int port) throws InterruptedException {
		this.port = port;
		this.run();
	}

	public RakNetPacketFactory getPacketFactory() {
		return packetFactory;
	}

	public PacketEncoder getEncoder() {
		return encoder;
	}

	public PacketDecoder getDecoder() {
		return decoder;
	}

	public int getPort() {
		return port;
	}

	public void run() throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ChannelFuture channel = new Bootstrap()
				.group(group)
				.option(ChannelOption.SO_REUSEADDR, true)
				.channel(NioDatagramChannel.class)
				.handler(new ChannelInitializer<NioDatagramChannel>() {
					@Override
					protected void initChannel(NioDatagramChannel channel) {
						channel.pipeline().addLast(
								new RakNetDecodeHandler(RakNetServer.this),
								new RakNetEncodeHandler(RakNetServer.this),
								new UnconnectedPingPacketHandler(RakNetServer.this)
						);
					}
				})
				.bind(getPort())
				.sync()
				.channel()
				.closeFuture();
	}

	public void sendPacket(InetSocketAddress address, RakNetPacket packet) {

	}
}
