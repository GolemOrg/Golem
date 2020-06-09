package net.golem.network.raknet.protocol;

import io.netty.buffer.ByteBuf;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Log4j2
public abstract class RakNetPacket implements DataPacket  {

	private byte id;

	public static final byte[] MAGIC = new byte[] { (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };

	public RakNetPacket(byte id) {
		this.id = id;
	}

	/**
	 * Read from the buffer
	 *
	 *
	 * @param decoder
	 */
	public abstract void decode(PacketDecoder decoder);

	/**
	 *
	 *
	 * @param encoder
	 */
	public abstract void encode(PacketEncoder encoder);

	/**
	 * Returns the packet ID
	 *
	 * @return byte
	 */
	public byte getId() {
		return this.id;
	}

	public ByteBuf write(PacketEncoder encoder) {
		encoder.writeByte(getId());
		this.encode(encoder);
		return encoder.getBuffer();
	}

}
