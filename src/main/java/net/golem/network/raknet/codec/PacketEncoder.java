package net.golem.network.raknet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class PacketEncoder {

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

	public void writeBoolean(boolean value) {
		getBuffer().writeBoolean(value);
	}

	public void writeShort(short value) {
		getBuffer().writeShort(value);
	}

	public void writeMedium(int value) {
		getBuffer().writeMedium(value);
	}

	public void writeInt(int value) {
		getBuffer().writeInt(value);
	}

	public void writeLong(long value) {
		getBuffer().writeLong(value);
	}

	public void writeChar(char value) {
		getBuffer().writeChar(value);
	}

	public void writeFloat(float value) {
		getBuffer().writeFloat(value);
	}

	public void writeDouble(double value) {
		getBuffer().writeDouble(value);
	}

	public void writeByte(byte value) {
		getBuffer().writeByte(value);
	}

	public void writeBytes(byte[] bytes) {
		getBuffer().writeBytes(bytes);
	}

	public void writeString(String string) {
		getBuffer().writeShort(string.length());
		getBuffer().writeCharSequence(string, StandardCharsets.UTF_8);
	}

	public void clear() {
		getBuffer().clear();
	}

}
