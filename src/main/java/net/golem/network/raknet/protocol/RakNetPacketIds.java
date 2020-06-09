package net.golem.network.raknet.protocol;

public final class RakNetPacketIds {

	public static final byte CONNECTED_PING = 0x00;
	public static final byte CONNECTED_PONG = 0x03;

	public static final byte UNCONNECTED_PING = 0x01;
	public static final byte UNCONNECTED_PONG = 0x1c;

	public static final byte OPEN_CONNECTION_REQUEST_1 = 0x05;
	public static final byte OPEN_CONNECTION_RESPONSE_1 = 0x06;

	public static final byte OPEN_CONNECTION_REQUEST_2 = 0x07;
	public static final byte OPEN_CONNECTION_RESPONSE_2 = 0x08;

	public static final byte CONNECTION_REQUEST = 0x09;
	public static final byte CONNECTION_REQUEST_ACCEPTED = 0x10;

	public static final byte NEW_INCOMING_CONNECTION = 0x13;
	public static final byte DISCONNECTION_REQUEST = 0x15;

	public static final byte INCOMPATIBLE_PROTOCOL_VERSION = 0x19;



}
