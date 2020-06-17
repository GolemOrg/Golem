package net.golem.network.protocol;

import io.netty.buffer.ByteBuf;
import net.golem.network.GamePacketIds;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.codec.PacketDecoder;

import java.util.LinkedHashMap;

public class GamePacketFactory {

	protected static LinkedHashMap<Integer, Class<? extends DataPacket>> packets = new LinkedHashMap<>();

	public static DataPacket from(ByteBuf buf) {
		try {
			PacketDecoder decoder = new PacketDecoder(buf);
			int packetId = decoder.readUnsignedByte();
			Class<? extends DataPacket> packetClass = packets.get(packetId);
			if(packetClass != null) {
				DataPacket packet = packetClass.newInstance();
				packet.decode(decoder);
				return packet;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void register() {
		packets.put(GamePacketIds.PACKET_BATCH, PacketBatch.class);
		packets.put(GamePacketIds.LOGIN_PACKET, LoginPacket.class);
	}
}
