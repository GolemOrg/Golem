package net.golem.network.protocol;

import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.raknet.utils.ByteBufUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Log4j2
public class PacketBatch extends DataPacket {

	public ArrayList<DataPacket> packets = new ArrayList<>();

	public PacketBatch() {
		super(GamePacketIds.PACKET_BATCH);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		DeflaterOutputStream deflater = new DeflaterOutputStream(new ByteBufOutputStream(encoder.getBuffer()));
		try {
			packets.forEach(packet -> {
				ByteBuf buffer = packet.create();
				try {
					deflater.write(writeUnsignedVarInt(buffer.readableBytes()));
					deflater.write(ByteBufUtils.array(buffer));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			deflater.flush();
			deflater.close();
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
		}
	}

	public byte[] writeUnsignedVarInt(int value) {
		ArrayList<Byte> output = new ArrayList<>();
		while ((value & ~0x7F) != 0) {
			output.add((byte) ((value & 0x7F) | 0x80));
			value >>>= 7;
		}
		output.add((byte) value);
		return Bytes.toArray(output);
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
