package net.golem.network.raknet.codec;

import io.netty.buffer.ByteBuf;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetAddressUtils;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@Log4j2
public class PacketDecoder {

	private ByteBuf buffer;

	public PacketDecoder(ByteBuf buffer) {
		this.buffer = buffer;
	}

	public ByteBuf getBuffer() {
		return buffer;
	}

	public int length() {
		return buffer.readableBytes();
	}

	public InetSocketAddress readAddress() {
		try {
			int type = this.readByte();
			byte[] addressBytes;
			int port;
			if(type == RakNetAddressUtils.IPV4) {
				addressBytes = new byte[RakNetAddressUtils.IPV4_ADDRESS_LENGTH];
				for(int i = 0; i < addressBytes.length; i++) addressBytes[i] = (byte) (~this.readByte() & 0xFF);
				port = this.readShort();
			} else if(type == RakNetAddressUtils.IPV6) {
				this.readShort(); // AF_INET6
				port = this.readShort();
				this.readInt(); // Flow Info
				addressBytes = this.readBytes(RakNetAddressUtils.IPV6_ADDRESS_LENGTH).array();
				this.readInt(); // Scope ID
			} else {
				throw new Exception(String.format("Unknown address type %s", type));
			}
			return new InetSocketAddress(InetAddress.getByAddress(addressBytes), port);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public boolean readBoolean() {
		return buffer.readBoolean();
	}

	public short readShort() {
		return buffer.readShort();
	}

	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}

	public int readUnsignedShortLE() {
		return buffer.readUnsignedShortLE();
	}

	public int readMedium() {
		return buffer.readMedium();
	}

	public int readUnsignedMedium() {
		return buffer.readUnsignedMedium();
	}

	public int readUnsignedMediumLE() {
		return buffer.readUnsignedMediumLE();
	}

	public int readInt() {
		return buffer.readInt();
	}

	public int readUnsignedInt() {
		return (int) buffer.readUnsignedInt();
	}

	public long readLong() {
		return buffer.readLong();
	}

	public char readChar() {
		return buffer.readChar();
	}

	public float readFloat() {
		return buffer.readFloat();
	}

	public double readDouble() {
		return buffer.readDouble();
	}

	public byte readByte() {
		return buffer.readByte();
	}

	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}

	public ByteBuf readBytes(int length) throws Exception {
		if(length < 0) {
			throw new Exception("Length cannot be less than 0");
		}

		if(buffer.readerIndex() + length > buffer.writerIndex()) {
			throw new Exception("Length exceeds the buffer length!");
		}

		return getBuffer().readBytes(length);
	}

	public ByteBuf readSlice(int length) {
		return buffer.readSlice(length).copy();
	}

	public void skipBytes(int length) {
		buffer.skipBytes(length);
	}

	public void skipReadable() {
		buffer.skipBytes(buffer.readableBytes());
	}

	public boolean isReadable() {
		return buffer.isReadable();
	}

	public ByteBuf readRemaining() throws Exception {
		return readBytes(this.length());
	}

	public void readMagic() {
		buffer.skipBytes(RakNetPacket.MAGIC.length);
	}

	public String readString() {
		short length = buffer.readShort();
		return (String) buffer.readCharSequence(length, StandardCharsets.UTF_8);
	}

}
