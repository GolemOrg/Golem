package net.golem.network.raknet.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.connection.OpenConnectionRequestPacket1;
import net.golem.network.raknet.protocol.connection.SessionPacket;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler extends RakNetInboundPacketHandler<SessionPacket> {

	private NioEventLoopGroup sessionGroup = new NioEventLoopGroup();

	private ConcurrentHashMap<InetSocketAddress, Session> sessions = new ConcurrentHashMap<>();

	private ChannelHandlerContext context;

	public SessionHandler(RakNetServer server) {
		super(server, SessionPacket.class);
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
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<SessionPacket> message) {
		if(this.context == null) {
			this.context = context;
		}
		SessionPacket packet = message.content();
		InetSocketAddress sender = message.sender();
		Session session;

		if(packet instanceof OpenConnectionRequestPacket1 && !this.contains(sender)) {
			try {
				session = this.create(message.sender());
			} catch (SessionException e) {
				e.printStackTrace();
				return;
			}
		} else {
			session = this.get(sender);
		}

		if(session != null) {
			session.handle(packet);
			return;
		}
	}

	public void closeAll() {
		for(Session session : getSessions().values()) {
			session.close();
		}
	}
}
