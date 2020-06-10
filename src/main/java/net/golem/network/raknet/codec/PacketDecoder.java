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
		return getBuffer().readableBytes();
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
				addressBytes = this.readBytes(RakNetAddressUtils.IPV6_ADDRESS_LENGTH);
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
		return getBuffer().readBoolean();
	}

	public short readShort() {
		return getBuffer().readShort();
	}

	public int readUnsignedShort() {
		return getBuffer().readUnsignedShort();
	}

	public int readMedium() {
		return getBuffer().readMedium();
	}

	public int readInt() {
		return getBuffer().readInt();
	}

	public int readUnsignedInt() {
		return (int) getBuffer().readUnsignedInt();
	}

	public long readLong() {
		return getBuffer().readLong();
	}

	public char readChar() {
		return getBuffer().readChar();
	}

	public float readFloat() {
		return getBuffer().readFloat();
	}

	public double readDouble() {
		return getBuffer().readDouble();
	}

	public byte readByte() {
		return getBuffer().readByte();
	}

	public int readUnsignedByte() {
		return getBuffer().readUnsignedByte();
	}

	public byte[] readBytes(int length) {
		byte[] output = new byte[length];

		//TODO: Find a better way to do this
		for(int i = 0; i < length; i++) {
			output[i] = getBuffer().readByte();
		}
		return output;
	}

	public void skipBytes(int length) {
		getBuffer().skipBytes(length);
	}

	public void skipReadable() {
		getBuffer().skipBytes(getBuffer().readableBytes());
	}

	public boolean isReadable() {
		return getBuffer().readableBytes() > 0;
	}

	public byte[] readRemaining() {
		return readBytes(this.length());
	}

	public void readMagic() {
		getBuffer().skipBytes(RakNetPacket.MAGIC.length);
	}



	public String readString() {
		short length = getBuffer().readShort();
		return (String) getBuffer().readCharSequence(length, StandardCharsets.UTF_8);
	}

}
