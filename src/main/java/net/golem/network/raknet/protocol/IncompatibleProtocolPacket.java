package net.golem.network.raknet.protocol;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

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
