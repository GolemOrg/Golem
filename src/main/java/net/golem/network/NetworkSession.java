package net.golem.network;

import java.net.InetSocketAddress;

public class NetworkSession {

	private InetSocketAddress address;

	public NetworkSession(InetSocketAddress address) {
		this.address = address;
	}


	public InetSocketAddress getAddress() {
		return address;
	}
}
