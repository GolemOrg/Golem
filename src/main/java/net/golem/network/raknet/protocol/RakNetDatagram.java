package net.golem.network.raknet.protocol;

import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.BitFlags;

import java.util.ArrayList;

@Log4j2
public class RakNetDatagram extends RakNetPacket {

	public static final int HEADER_SIZE = 1 + 3; // header flags (1) + sequence number (3)

	public int headerFlags;

	public ArrayList<EncapsulatedPacket> packets = new ArrayList<>();

	public int sequenceNumber;

	public RakNetDatagram() {
		super(BitFlags.VALID.getId());
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.getBuffer().writeMediumLE(sequenceNumber);
		for(EncapsulatedPacket packet : packets) {
			packet.encode(encoder);
		}
	}

	@Override
	public void encodeHeader(PacketEncoder encoder) {
		encoder.writeByte((byte) (BitFlags.VALID.getId() | this.headerFlags));
	}

	@Override
	public void decode(PacketDecoder decoder) {
		decoder.getBuffer().resetReaderIndex();
		this.decodeHeader(decoder);
		this.sequenceNumber = decoder.readUnsignedMediumLE();
		while(decoder.isReadable()) {
			EncapsulatedPacket packet = new EncapsulatedPacket();
			packet.decode(decoder);
			this.packets.add(packet);
		}
	}

	@Override

	public void decodeHeader(PacketDecoder decoder) {
		this.headerFlags = decoder.readUnsignedByte();
	}

	public int length() {
		int length = HEADER_SIZE;
		for(EncapsulatedPacket packet : packets) {
			length += packet.getTotalLength();
		}

		return length;
	}

	@Override
	public String toString() {
		return "RakNetDatagram{" +
				"headerFlags=" + headerFlags +
				", packets=" + packets +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}