package net.golem.network.raknet.protocol;

import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.protocol.connection.request.OpenConnectionRequest1Packet;
import net.golem.network.raknet.protocol.connection.request.OpenConnectionRequest2Packet;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPingPacket;

import java.util.HashMap;

public class RakNetPacketFactory {

	private RakNetServer rakNet;

	private HashMap<Byte, Class<? extends RakNetPacket>> packets = new HashMap<>();

	public RakNetPacketFactory(RakNetServer rakNet) {
		this.rakNet = rakNet;
		this.registerPackets();
	}

	public boolean isRegistered(byte id) {
		return packets.get(id) != null;
	}

	public RakNetPacket getPacket(byte id) {
		try {
			return packets.get(id).newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void registerPackets() {
		this.register(RakNetPacketIds.UNCONNECTED_PING, UnconnectedPingPacket.class);
		this.register(RakNetPacketIds.OPEN_CONNECTION_REQUEST_1, OpenConnectionRequest1Packet.class);
		this.register(RakNetPacketIds.OPEN_CONNECTION_REQUEST_2, OpenConnectionRequest2Packet.class);
	}

	public void register(byte id, Class<? extends RakNetPacket> packet) {
		packets.put(id, packet);
	}

}
