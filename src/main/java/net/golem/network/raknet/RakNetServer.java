package net.golem.network.raknet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import net.golem.network.raknet.handler.codec.RakNetDecodeHandler;
import net.golem.network.raknet.handler.packet.OpenConnectionRequest1Handler;
import net.golem.network.raknet.handler.packet.OpenConnectionRequest2Handler;
import net.golem.network.raknet.handler.packet.UnconnectedPingHandler;
import net.golem.network.raknet.session.SessionManager;


public class RakNetServer {

	private SessionManager sessionManager;

	private boolean needsContext = true;

	private NioEventLoopGroup group;

	private ChannelHandlerContext context;

	private long startTime;

	private final int port;

	public RakNetServer(int port) throws InterruptedException {
		this.port = port;
		this.startTime = System.currentTimeMillis();
		this.run();
	}

	public boolean needsContext() {
		return this.needsContext;
	}

	public void setContext(ChannelHandlerContext context) {
		this.context = context;
		this.needsContext = false;
	}

	public ChannelHandlerContext getContext() {
		return context;
	}

	public int getPort() {
		return port;
	}

	public void run() throws InterruptedException {
		this.group = new NioEventLoopGroup();
		new Bootstrap()
				.group(this.group)
				.option(ChannelOption.SO_REUSEADDR, true)
				.channel(NioDatagramChannel.class)
				.handler(new ChannelInitializer<NioDatagramChannel>() {
					@Override
					protected void initChannel(NioDatagramChannel channel) {
						channel.pipeline().addLast(
								new RakNetDecodeHandler(RakNetServer.this),
								new UnconnectedPingHandler(RakNetServer.this),
								new OpenConnectionRequest1Handler(RakNetServer.this),
								new OpenConnectionRequest2Handler(RakNetServer.this)
						);
					}
				})
				.bind(getPort())
				.sync()
				.channel()
				.closeFuture();
		this.sessionManager = new SessionManager(this);
	}

	public NioEventLoopGroup getGroup() {
		return group;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public long getRakNetTimeMS() {
		return System.currentTimeMillis() - this.startTime;
	}

}
