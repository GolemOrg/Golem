package net.golem.network.raknet.protocol.connection;

import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.RakNetAddressUtils;
import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;
import net.golem.network.raknet.types.AddressCount;

import java.net.InetSocketAddress;

@Log4j2
public class ConnectionRequestAcceptedPacket extends RakNetPacket {

	public InetSocketAddress clientAddress;

	public InetSocketAddress[] addresses = new InetSocketAddress[AddressCount.MINECRAFT.getCount()];

	public long sendPingTime;
	public long sendPongTime;

	public ConnectionRequestAcceptedPacket() {
		super(RakNetPacketIds.CONNECTION_REQUEST_ACCEPTED);
		addresses[0] = new InetSocketAddress("127.0.0.1", 0);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		clientAddress = decoder.readAddress();
		decoder.readShort(); // system index? no one knows what these *actually* do though
		int length = decoder.length();
		for(int i = 0; i < AddressCount.MINECRAFT.getCount(); ++i) {
			this.addresses[i] = decoder.getBuffer().readerIndex() + 16 < length ? decoder.readAddress() : RakNetAddressUtils.SYSTEM_ADDRESS;
		}

		this.sendPingTime = decoder.readLong();
		this.sendPongTime = decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeAddress(clientAddress);
		encoder.writeShort((short) 0);
		for (InetSocketAddress address : addresses) {
			InetSocketAddress currentAddress = address != null ? address : RakNetAddressUtils.SYSTEM_ADDRESS;
			encoder.writeAddress(currentAddress);
		}
		encoder.writeLong(sendPingTime);
		encoder.writeLong(sendPongTime);
	}
}