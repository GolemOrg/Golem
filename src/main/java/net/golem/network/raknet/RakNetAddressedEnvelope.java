package net.golem.network.raknet;

import io.netty.channel.DefaultAddressedEnvelope;

import java.net.InetSocketAddress;

public class RakNetAddressedEnvelope<I extends DataPacket> extends DefaultAddressedEnvelope<I, InetSocketAddress> {

	public RakNetAddressedEnvelope(I packet, InetSocketAddress recipient, InetSocketAddress sender) {
		super(packet, recipient, sender);
	}

	public RakNetAddressedEnvelope(I packet, InetSocketAddress recipient) {
		super(packet, recipient);
	}
}
