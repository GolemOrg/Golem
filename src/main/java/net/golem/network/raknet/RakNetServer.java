package net.golem.network.raknet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.golem.network.raknet.handler.codec.RakNetDecodeHandler;
import net.golem.network.raknet.handler.codec.RakNetEncodeHandler;
import net.golem.network.raknet.handler.packet.OpenConnectionReply1Handler;
import net.golem.network.raknet.handler.packet.OpenConnectionReply2Handler;
import net.golem.network.raknet.handler.packet.UnconnectedPingHandler;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketFactory;
import net.golem.network.raknet.session.SessionManager;

import java.net.InetSocketAddress;

public class RakNetServer {

	private RakNetPacketFactory packetFactory = new RakNetPacketFactory(this);

	private SessionManager sessionManager;

	private final int port;

	public RakNetServer(int port) throws InterruptedException {
		this.port = port;
		this.run();
	}

	public RakNetPacketFactory getPacketFactory() {
		return packetFactory;
	}

	public int getPort() {
		return port;
	}

	public void run() throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		new Bootstrap()
				.group(group)
				.option(ChannelOption.SO_REUSEADDR, true)
				.channel(NioDatagramChannel.class)
				.handler(new ChannelInitializer<NioDatagramChannel>() {
					@Override
					protected void initChannel(NioDatagramChannel channel) {
						channel.pipeline().addLast(
								new RakNetDecodeHandler(RakNetServer.this),
								new UnconnectedPingHandler(RakNetServer.this),
								new OpenConnectionReply1Handler(RakNetServer.this),
								new OpenConnectionReply2Handler(RakNetServer.this),
								new RakNetEncodeHandler(RakNetServer.this)
						);
					}
				})
				.bind(getPort())
				.sync()
				.channel()
				.closeFuture();
		this.sessionManager = new SessionManager(this);
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}
}
