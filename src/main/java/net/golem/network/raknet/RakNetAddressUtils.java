package net.golem.network.raknet;

import java.net.InetAddress;

public class RakNetAddressUtils {

	public static int IPV4 = 4;
	public static int IPV6 = 6;

	public static final int AF_INET6 = 10;

	public static int IPV4_ADDRESS_LENGTH = 4;
	public static int IPV6_ADDRESS_LENGTH = 16;

	public static int getAddressVersion(InetAddress address) {
		if(address == null) throw new NullPointerException("Address is null");
		int length = address.getAddress().length;
		if(length == IPV4_ADDRESS_LENGTH) {
			return IPV4;
		} else if(length == IPV6_ADDRESS_LENGTH) {
			return IPV6;
		}
		return -1;
	}
}
