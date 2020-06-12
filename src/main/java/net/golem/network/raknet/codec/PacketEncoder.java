package net.golem.network.raknet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetAddressUtils;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@Log4j2
public class PacketEncoder {

	private ByteBuf buffer = Unpooled.buffer();

	public ByteBuf getBuffer() {
		return buffer;
	}

	public int length() {
		return buffer.capacity();
	}

	public void writeBoolean(boolean value) {
		buffer.writeBoolean(value);
	}

	public void writeAddress(InetSocketAddress address) {
		try {
			if(address == null || address.getAddress() == null) {
				throw new NullPointerException("Address or IP address null");
			}
			ByteBuf addressBytes = Unpooled.copiedBuffer(address.getAddress().getAddress());
			this.writeByte((byte) RakNetAddressUtils.getAddressVersion(address.getAddress()));
			if(address.getAddress() instanceof Inet4Address) {
				flip(addressBytes);
				this.writeBytes(addressBytes);
				this.writeShort((short) address.getPort());
			} else if(address.getAddress() instanceof Inet6Address) {
				this.writeByte((byte) RakNetAddressUtils.AF_INET6);
				this.writeShortLE((short) address.getPort());
				this.writeInt(0x00); // Flow info
				this.writeBytes(addressBytes);
				this.writeInt(((Inet6Address) address.getAddress()).getScopeId()); //Scope ID
			} else {
				throw new Exception("Unknown InetAddress type");
			}
			this.writeShort((short) address.getPort());
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	public void flip(ByteBuf buffer) {
		for(int i = 0; i < buffer.capacity(); i++) {
			buffer.setByte(i, ~(buffer.getByte(i) & 0xFF));
		}
	}

	public void writeShort(short value) {
		buffer.writeShort(value);
	}

	public void writeShortLE(short value) {
		buffer.writeShortLE(value);
	}

	public void writeMedium(int value) {
		buffer.writeMedium(value);
	}

	public void writeMediumLE(int value) {
		buffer.writeMediumLE(value);
	}

	public void writeInt(int value) {
		buffer.writeInt(value);
	}

	public void writeLong(long value) {
		buffer.writeLong(value);
	}

	public void writeChar(char value) {
		buffer.writeChar(value);
	}

	public void writeFloat(float value) {
		buffer.writeFloat(value);
	}

	public void writeDouble(double value) {
		buffer.writeDouble(value);
	}

	public void writeByte(byte value) {
		buffer.writeByte(value);
	}

	public void writeBytes(ByteBuf bytes) {
		buffer.writeBytes(bytes);
	}

	public void writeString(String string) {
		buffer.writeShort(string.length());
		buffer.writeCharSequence(string, StandardCharsets.UTF_8);
	}

	public void writeMagic() {
		buffer.writeBytes(RakNetPacket.MAGIC);
	}

	public void writeZeroes(int count) {

	}

	public void clear() {
		buffer.clear();
	}

}