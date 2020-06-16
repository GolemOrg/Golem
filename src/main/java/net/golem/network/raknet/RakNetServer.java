package net.golem.network.raknet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.handler.codec.RakNetDecodeHandler;
import net.golem.network.raknet.handler.packet.OpenConnectionRequest1Handler;
import net.golem.network.raknet.handler.packet.OpenConnectionRequest2Handler;
import net.golem.network.raknet.handler.packet.UnconnectedPingHandler;
import net.golem.network.raknet.identifier.Identifier;
import net.golem.network.raknet.session.RakNetSession;
import net.golem.network.raknet.session.SessionManager;

import java.net.InetSocketAddress;
import java.util.UUID;


@Log4j2
public class RakNetServer {

	public static final int PROTOCOL_VERSION = 9;

	private static RakNetServer instance;

	private SessionManager sessionManager;

	private boolean running = false;
	private boolean needsContext = true;

	private NioEventLoopGroup group;
	private ChannelHandlerContext context;

	private long startTime;

	private UUID guid;

	private Identifier identifier;

	private final InetSocketAddress local;

	public RakNetServer(String host, int port, Identifier identifier) throws InterruptedException {
		instance = this;
		this.local = new InetSocketAddress(host, port);
		this.startTime = System.currentTimeMillis();
		this.guid = UUID.randomUUID();
		this.identifier = identifier;
		this.run();
	}

	public static RakNetServer getInstance() {
		return instance;
	}

	public boolean needsContext() {
		return this.needsContext;
	}

	public boolean isRunning() {
		return running;
	}

	public void setContext(ChannelHandlerContext context) {
		this.context = context;
		this.needsContext = false;
	}

	public ChannelHandlerContext getContext() {
		return context;
	}

	public void run() throws InterruptedException {
		this.group = new NioEventLoopGroup();
		new Bootstrap()
			.group(this.group)
			.option(ChannelOption.SO_REUSEADDR, true)
			.channel(NioDatagramChannel.class)
			.handler( new ChannelInitializer<NioDatagramChannel>() {
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
			.bind(local)
			.sync();
		this.sessionManager = new SessionManager(this);
		this.running = true;
	}

	public void shutdown(String reason) {
		if(!this.isRunning()) {
			return;
		}

		this.running = false;
		getSessionManager().getSessions().values().forEach(RakNetSession::close);
		group.shutdownGracefully();
		log.info("Shutting down RakNet" + (reason != null ? String.format(" for reason: %s", reason) : "") + "...");
	}

	public void shutdown() {
		shutdown(null);
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public long getRakNetTimeMS() {
		return System.currentTimeMillis() - this.startTime;
	}

	public UUID getGlobalUniqueId() {
		return guid;
	}

	public Identifier getIdentifier() {
		return identifier;
	}
}