package net.golem.network.raknet.protocol;

import net.golem.network.raknet.RakNetServer;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPingPacket;
import net.golem.network.raknet.protocol.unconnected.UnconnectedPongPacket;

import java.util.HashMap;

public class RakNetPacketFactory {

	private RakNetServer rakNet;

	private HashMap<Byte, Class<? extends RakNetPacket>> packets = new HashMap<>();

	public RakNetPacketFactory(RakNetServer rakNet) {
		this.rakNet = rakNet;
		this.registerPackets();
	}

	public RakNetServer getRakNet() {
		return rakNet;
	}

	public HashMap<Byte, Class<? extends RakNetPacket>> getPackets() {
		return packets;
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
		this.register(RakNetPacketIds.UNCONNECTED_PONG, UnconnectedPongPacket.class);
	}

	public void register(byte id, Class<? extends RakNetPacket> packet) {
		packets.put(id, packet);
	}

}
