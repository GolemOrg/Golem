package net.golem.network.raknet;

import io.netty.channel.DefaultAddressedEnvelope;
import net.golem.network.raknet.protocol.RakNetPacket;

import java.net.InetSocketAddress;

public class RakNetAddressedEnvelope<I extends RakNetPacket> extends DefaultAddressedEnvelope<I, InetSocketAddress> {

	public RakNetAddressedEnvelope(I packet, InetSocketAddress recipient, InetSocketAddress sender) {
		super(packet, recipient, sender);
	}

	public RakNetAddressedEnvelope(I packet, InetSocketAddress recipient) {
		super(packet, recipient);
	}
}
