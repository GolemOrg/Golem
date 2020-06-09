package net.golem.network.raknet.protocol.connection.request;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class OpenConnectionRequest1Packet extends RakNetPacket {

	public static final int MTU_PADDING = 28;

	public byte networkProtocol;

	public short maximumTransferUnits;

	public OpenConnectionRequest1Packet() {
		super(RakNetPacketIds.OPEN_CONNECTION_REQUEST_1);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		decoder.readMagic();
		this.networkProtocol = decoder.readByte();
		this.maximumTransferUnits = (short) ((short) decoder.length() + MTU_PADDING);
		decoder.skipReadable();
	}

	@Override
	public void encode(PacketEncoder encoder) {

	}
}
