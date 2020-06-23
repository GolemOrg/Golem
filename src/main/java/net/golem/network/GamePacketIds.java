package net.golem.network;

public final class GamePacketIds {

	public static final int LOGIN_PACKET = 0x01;
	public static final int PLAY_STATUS_PACKET = 0x02;

	public static final int SERVER_TO_CLIENT_HANDSHAKE_PACKET = 0x03;
	public static final int CLIENT_TO_SERVER_HANDSHAKE_PACKET = 0x04;

	public static final int DISCONNECT_PACKET = 0x05;

	public static final int RESOURCE_PACKS_INFO_PACKET = 0x06;
	public static final int RESOURCE_PACKS_STACK_PACKET = 0x07;
	public static final int RESOURCE_PACKS_RESPONSE_PACKET = 0x08;

	public static final int TEXT_PACKET = 0x09;
	public static final int SET_TIME_PACKET = 0x0A;
	public static final int START_GAME_PACKET = 0x0B;

	public static final int ADD_PLAYER_PACKET = 0x0C;

	public static final int ADD_ACTOR_PACKET = 0x0D;
	public static final int REMOVE_ACTOR_PACKET = 0x0E;

	public static final int ADD_ITEM_ACTOR_PACKET = 0x0F;
	public static final int TAKE_ITEM_ACTOR_PACKET = 0x11;

	public static final int MOVE_ACTOR_ABSOLUTE_PACKET = 0x12;
	public static final int MOVE_PLAYER_PACKET = 0x13;
	public static final int RIDER_JUMP_PACKET = 0x14;

	public static final int UPDATE_BLOCK_PACKET = 0x15;
	public static final int ADD_PAINTING_PACKET = 0x16;

	public static final int TICK_SYNC_CLIENT = 0x17;



	public static final int PACKET_BATCH = 0xFE;

}
