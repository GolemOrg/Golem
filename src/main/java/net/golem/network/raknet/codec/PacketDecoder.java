package net.golem.network.raknet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class PacketDecoder {

	private ByteBuf buffer = Unpooled.buffer();

	public ByteBuf getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuf buffer) {
		this.buffer = buffer;
	}

	public int length() {
		return buffer.capacity();
	}

	public boolean readBoolean() {
		return getBuffer().readBoolean();
	}

	public short readShort() {
		return getBuffer().readShort();
	}

	public int readMedium() {
		return getBuffer().readMedium();
	}

	public int readInt() {
		return getBuffer().readInt();
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

	public byte[] readBytes(int length) {
		return getBuffer().readBytes(length).array();
	}

	public void skipBytes(int length) {
		getBuffer().skipBytes(length);
	}

	public String readString() {
		short length = getBuffer().readShort();
		return (String) getBuffer().readCharSequence(length, StandardCharsets.UTF_8);
	}

	public void clear() {
		getBuffer().clear();
	}
}
