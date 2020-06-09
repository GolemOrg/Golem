package net.golem.network.raknet.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.connection.request.OpenConnectionRequest1Packet;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager extends RakNetInboundPacketHandler<RakNetPacket> {

	private NioEventLoopGroup sessionGroup = new NioEventLoopGroup();

	private ConcurrentHashMap<InetSocketAddress, Session> sessions = new ConcurrentHashMap<>();

	private ChannelHandlerContext context;

	public SessionManager(RakNetServer server) {
		super(server, RakNetPacket.class);
	}

	public NioEventLoopGroup getSessionGroup() {
		return sessionGroup;
	}

	public ConcurrentHashMap<InetSocketAddress, Session> getSessions() {
		return sessions;
	}

	public boolean contains(InetSocketAddress address) {
		return sessions.containsKey(address);
	}

	public Session get(InetSocketAddress address) {
		return this.get(address, false);
	}

	public Session get(InetSocketAddress address, boolean create) {
		if(!this.contains(address) && create) {
			try {
				this.create(address);
			} catch (SessionException e) {
				e.printStackTrace();
			}
		}
		return this.contains(address) ? sessions.get(address) : null;
	}

	public Session create(InetSocketAddress socketAddress) throws SessionException {
		if(this.contains(socketAddress)) {
			throw new SessionException("Session already exists!");
		}
		//we need to create a session here, but let's wait until the RakNetSession is complete
		return null;
	}

	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<RakNetPacket> message) {
		if(this.context == null) {
			this.context = context;
		}
		RakNetPacket packet = message.content();
		InetSocketAddress sender = message.sender();
		Session session;

	}

	public void closeAll() {
		for(Session session : getSessions().values()) {
			session.close();
		}
	}
}
