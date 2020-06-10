package net.golem.network.raknet.protocol.connection;

import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;
import net.golem.network.raknet.types.AddressCount;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

@Log4j2
public class ConnectionRequestAcceptedPacket extends RakNetPacket {

	public InetSocketAddress address;

	public InetSocketAddress[] addresses = new InetSocketAddress[AddressCount.MINECRAFT.getCount()];

	public long sendPingTime;
	public long sendPongTime;

	public ConnectionRequestAcceptedPacket() {
		super(RakNetPacketIds.CONNECTION_REQUEST_ACCEPTED);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		address = decoder.readAddress();
		decoder.readShort(); //what is this short?
		int length = decoder.length();
		InetSocketAddress dummy = new InetSocketAddress("0.0.0.0", 0);
		for(int i = 0; i < AddressCount.MINECRAFT.getCount(); ++i) {
			this.addresses[i] = decoder.getBuffer().readerIndex() + 16 < length ? decoder.readAddress() : dummy;
		}

		this.sendPingTime = decoder.readLong();
		this.sendPongTime = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeAddress(address);
		encoder.writeShort((short) addresses.length);
		InetSocketAddress dummy = new InetSocketAddress("0.0.0.0", 0);
		for (InetSocketAddress inetSocketAddress : addresses) {
			encoder.writeAddress(inetSocketAddress != null ? inetSocketAddress : dummy);
		}

		encoder.writeLong(sendPingTime);
		encoder.writeLong(sendPongTime);
	}
}
