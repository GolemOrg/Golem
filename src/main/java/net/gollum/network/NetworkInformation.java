package net.gollum.network;


public class NetworkInformation {

	public String address;

	public int port;

	public NetworkInformation(String address, int port) {
		this.address = address;
		this.port = port;
	}


	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

}
