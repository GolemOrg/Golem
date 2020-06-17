package net.golem.network.raknet.session;

import io.netty.channel.ChannelHandlerContext;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager extends RakNetInboundPacketHandler<DataPacket> {

	private ConcurrentHashMap<InetSocketAddress, RakNetSession> sessions = new ConcurrentHashMap<>();

	private Class<? extends RakNetSession> sessionInterface = RakNetSession.class;

	public SessionManager(RakNetServer server) {
		super(server, RakNetPacket.class);
	}

	public ConcurrentHashMap<InetSocketAddress, RakNetSession> getSessions() {
		return sessions;
	}

	public Class<? extends RakNetSession> getSessionInterface() {
		return sessionInterface;
	}

	public void setSessionInterface(Class<? extends RakNetSession> sessionInterface) {
		this.sessionInterface = sessionInterface;
	}

	public boolean contains(InetSocketAddress address) {
		return sessions.containsKey(address);
	}

	public RakNetSession get(InetSocketAddress address) {
		return this.get(address, true);
	}

	public RakNetSession get(InetSocketAddress address, boolean create) {
		if(!this.contains(address) && create) {
			try {
				this.create(address);
			} catch (SessionException e) {
				e.printStackTrace();
			}
		}
		return this.contains(address) ? sessions.get(address) : null;
	}

	public RakNetSession create(InetSocketAddress socketAddress) throws SessionException {
		if(this.contains(socketAddress)) {
			throw new SessionException("Session already exists!");
		}
		RakNetSession session;
		try {
			session = getSessionInterface()
					.getConstructor(RakNetServer.class, SessionManager.class, InetSocketAddress.class, ChannelHandlerContext.class)
					.newInstance(this.getRakNet(), this, socketAddress, this.getRakNet().getContext());
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
		this.sessions.put(socketAddress, session);
		return session;
	}

	public void remove(RakNetSession session) {
		if(this.contains(session.getAddress())) {
			this.getSessions().remove(session.getAddress());
		}
	}

	@Override
	protected void handlePacket(ChannelHandlerContext context, RakNetAddressedEnvelope<DataPacket> message) {

	}

	public void closeAll() {
		getSessions().values().forEach(RakNetSession::close);
	}
}
