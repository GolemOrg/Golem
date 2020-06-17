package net.golem.network.raknet.protocol;

import net.golem.network.raknet.BitFlags;

public final class RakNetPacketIds {

	public static final int CONNECTED_PING = 0x00;
	public static final int CONNECTED_PONG = 0x03;

	public static final int UNCONNECTED_PING = 0x01;
	public static final int UNCONNECTED_PONG = 0x1c;

	public static final int OPEN_CONNECTION_REQUEST_1 = 0x05;
	public static final int OPEN_CONNECTION_REPLY_1 = 0x06;

	public static final int OPEN_CONNECTION_REQUEST_2 = 0x07;
	public static final int OPEN_CONNECTION_REPLY_2 = 0x08;

	public static final int CONNECTION_REQUEST = 0x09;
	public static final int CONNECTION_REQUEST_ACCEPTED = 0x10;

	public static final int NEW_INCOMING_CONNECTION = 0x13;
	public static final int DISCONNECTION_REQUEST = 0x15;

	public static final int INCOMPATIBLE_PROTOCOL_VERSION = 0x19;

	public static final int ACK = 0xc0;
	public static final int NAK = 0xa0;

	public static final int FRAME_SET = BitFlags.VALID.getId() | BitFlags.NEEDS_B_AND_AS.getId();


}