package net.golem.network.raknet.protocol.connection.request;

import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

import java.net.InetSocketAddress;

@Log4j2
public class OpenConnectionRequest2Packet extends RakNetPacket {

	public long clientId;

	public InetSocketAddress serverAddress;

	public short maximumTransferUnits;

	public OpenConnectionRequest2Packet() {
		super(RakNetPacketIds.OPEN_CONNECTION_REQUEST_2);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		decoder.readMagic();
		serverAddress = decoder.readAddress();
		maximumTransferUnits = decoder.readShort();
		clientId = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeMagic();
		encoder.writeAddress(serverAddress);
		encoder.writeShort(maximumTransferUnits);
		encoder.writeLong(clientId);
	}
}
