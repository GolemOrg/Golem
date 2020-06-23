package net.golem.network.protocol;

import io.netty.buffer.ByteBuf;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.protocol.RawPacket;

import java.util.LinkedHashMap;

@Log4j2
public class GamePacketFactory {

	protected static LinkedHashMap<Integer, Class<? extends DataPacket>> packets = new LinkedHashMap<>();

	public static DataPacket from(ByteBuf buffer) {
		try {
			PacketDecoder decoder = new PacketDecoder(buffer);
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

	public static DataPacket readRaw(RawPacket raw) {
		try {
			PacketDecoder decoder = new PacketDecoder(raw.buffer);
			Class<? extends DataPacket> packetClass = packets.get(raw.getPacketId());
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
		packets.put(GamePacketIds.LOGIN_PACKET, LoginPacket.class);
	}
}
