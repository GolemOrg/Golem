package net.golem.network.protocol;

import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;

@Log4j2
public class PacketBatch extends DataPacket {

	public ArrayList<DataPacket> packets = new ArrayList<>();

	public PacketBatch() {
		super(GamePacketIds.PACKET_BATCH);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		PacketEncoder internalEncoder = new PacketEncoder();
		for(DataPacket packet : packets) {
			ByteBuf buffer = packet.write(internalEncoder);
			int length = buffer.readableBytes();
			encoder.writeInt(length);
			internalEncoder.writeBytes(buffer);
		}
		DeflaterInputStream deflater = new DeflaterInputStream(new ByteBufInputStream(internalEncoder.getBuffer()));
		try {
			Unpooled.wrappedBuffer(ByteStreams.toByteArray(deflater));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void decode(PacketDecoder decoder) {
		packets.clear();
		InflaterInputStream inflater = new InflaterInputStream(new ByteBufInputStream(decoder.getBuffer()));
		try {
			ByteBuf buffer = Unpooled.wrappedBuffer(ByteStreams.toByteArray(inflater));
			while(buffer.isReadable()) {
				int length = this.readUnsignedVarInt(buffer);
				packets.add(GamePacketFactory.from(buffer.readBytes(length)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			decoder.getBuffer().release();
		}
	}

	public int readUnsignedVarInt(ByteBuf buffer) {
		int value = 0;
		int size = 0;
		int b;
		while (((b = buffer.readByte()) & 0x80) == 0x80) {
			value |= (b & 0x7F) << (size++ * 7);
			if (size >= 5) {
				throw new IllegalArgumentException("VarInt too big");
			}
		}
		return value | ((b & 0x7F) << (size * 7));
	}
}
