package net.golem.network.raknet.protocol.connection.reply;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

import java.net.InetSocketAddress;

public class OpenConnectionReply2Packet extends RakNetPacket {

	public long serverId;

	public InetSocketAddress clientAddress;

	public int maximumTransferUnits;

	public boolean serverSecurity = false;

	public OpenConnectionReply2Packet() {
		super(RakNetPacketIds.OPEN_CONNECTION_REPLY_2);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		decoder.readMagic();
		serverId = decoder.readLong();
		clientAddress = decoder.readAddress();
		maximumTransferUnits = decoder.readUnsignedShort();
		serverSecurity = decoder.readBoolean();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeMagic();
		encoder.writeLong(serverId);
		encoder.writeAddress(clientAddress);
		encoder.writeShort((short) maximumTransferUnits);
		encoder.writeBoolean(serverSecurity);
	}
}