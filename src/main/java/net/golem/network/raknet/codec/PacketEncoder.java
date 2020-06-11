package net.golem.network.raknet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetAddressUtils;
import net.golem.network.raknet.protocol.RakNetPacket;

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
			int type = RakNetAddressUtils.getAddressVersion(address.getAddress());
			ByteBuf addressBytes = Unpooled.copiedBuffer(address.getAddress().getAddress());
			this.writeByte((byte) type);
			if(type == RakNetAddressUtils.IPV4) {
				for (byte addressByte : addressBytes.array()) this.writeByte((byte) (~addressByte & 0xFF));
				this.writeShort((short) address.getPort());
			} else if(type == RakNetAddressUtils.IPV6) {
				this.writeShort((short) RakNetAddressUtils.AF_INET6);
				this.writeShort((short) address.getPort());
				this.writeInt(0x00); // Flow info
				this.writeBytes(addressBytes);
				this.writeInt(0x00); //Scope ID
			} else {
				throw new Exception(String.format("Unknown IP type %s", type));
			}
			this.writeShort((short) address.getPort());
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	public void writeShort(short value) {
		buffer.writeShort(value);
	}

	public void writeMedium(int value) {
		buffer.writeMedium(value);
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