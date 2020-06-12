package net.golem.network.raknet.protocol.connection;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;

public class IncompatibleProtocolPacket extends RakNetPacket {

	public int protocolVersion;
	public long guid;

	public IncompatibleProtocolPacket() {
		super(RakNetPacketIds.INCOMPATIBLE_PROTOCOL_VERSION);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		protocolVersion = decoder.readUnsignedByte();
		decoder.readMagic();
		guid = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeByte((byte) protocolVersion);
		encoder.writeMagic();
		encoder.writeLong(guid);
	}
}
