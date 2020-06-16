package net.golem.network.raknet.session;

import io.netty.channel.ChannelHandlerContext;
import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.RakNetAddressedEnvelope;
import net.golem.network.raknet.handler.RakNetInboundPacketHandler;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager extends RakNetInboundPacketHandler<DataPacket> {

	private ConcurrentHashMap<InetSocketAddress, RakNetSession> sessions = new ConcurrentHashMap<>();

	public SessionManager(RakNetServer server) {
		super(server, RakNetPacket.class);
	}

	public ConcurrentHashMap<InetSocketAddress, RakNetSession> getSessions() {
		return sessions;
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
		RakNetSession session = new RakNetSession(this.getRakNet(), this, socketAddress, this.getRakNet().getContext());
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
		DataPacket packet = message.content();
		InetSocketAddress sender = message.sender();
		RakNetSession session;

	}

	public void closeAll() {
		for(RakNetSession session : getSessions().values()) {
			session.close();
		}
	}
}
