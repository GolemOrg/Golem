package net.golem.network.raknet;

import io.netty.buffer.ByteBuf;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

public final class BufferUtils {

	public static byte[] dump(PacketEncoder encoder) {
		return dump(encoder.getBuffer());
	}

	public static byte[] dump(PacketDecoder decoder) {
		return dump(decoder.getBuffer());
	}

	public static byte[] dump(ByteBuf buf) {
		// save current reader & writer index
		int currentReader = buf.readerIndex();
		// reset reader index
		buf.resetReaderIndex();
		// create byte array
		byte[] output = new byte[buf.readableBytes()];
		for(int i = 0; i < output.length; i++) {
			output[i] = buf.readByte();
		}
		// reload the reader index
		buf.readerIndex(currentReader);
		return output;
	}

	public static String convert(byte[] array) {
		StringBuilder builder = new StringBuilder(array.length * 3);
		for(byte current : array) {
			builder.append(String.format("%02X ", current));
		}
		return builder.toString();
	}

	public static String dumpAndConvert(ByteBuf buf) {
		return convert(dump(buf));
	}

}
