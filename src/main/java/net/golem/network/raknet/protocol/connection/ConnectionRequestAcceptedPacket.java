package net.golem.network.raknet.protocol.connection;

import net.golem.network.raknet.codec.PacketDecoder;
import net.golem.network.raknet.codec.PacketEncoder;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.protocol.RakNetPacketIds;
import net.golem.network.raknet.types.AddressCount;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ConnectionRequestAcceptedPacket extends RakNetPacket {

	public InetSocketAddress address;

	public ArrayList<InetSocketAddress> systemAddresses = new ArrayList<>();

	public int sendPingTime;
	public int sendPongTime;

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
			this.systemAddresses.add(decoder.getBuffer().readerIndex() + 16 < length ? decoder.readAddress() : dummy);
		}

		this.sendPingTime = (int) decoder.readLong();
		this.sendPongTime = (int) decoder.readLong();
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeAddress(address);
		encoder.writeShort((short) 0);

		InetSocketAddress dummy = new InetSocketAddress("0.0.0.0", 0);
		for(int i = 0; i < AddressCount.MINECRAFT.getCount(); ++i) {
			InetSocketAddress address = systemAddresses.get(i);
			encoder.writeAddress(address != null ? address : dummy);
		}

		encoder.writeLong(sendPingTime);
		encoder.writeLong(sendPongTime);
	}
}
