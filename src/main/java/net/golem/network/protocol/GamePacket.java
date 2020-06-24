package net.golem.network.protocol;

import net.golem.math.Vector2f;
import net.golem.math.Vector3f;
import net.golem.network.session.GameSessionAdapter;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.raknet.protocol.DataPacket;
import net.golem.types.GameRule;
import net.golem.world.WorldSettings;

import java.util.ArrayList;

public abstract class GamePacket extends DataPacket {

	public GamePacket(int packetId) {
		super(packetId);
	}

	/**
	 * Just call the method adapter.handle(this); for any packet
	 * This is needed to redirect to the right GameSessionAdapter::handle method
	 * @param adapter
	 */
	public abstract boolean handle(GameSessionAdapter adapter);

	public void writeVector3f(PacketEncoder encoder, Vector3f vector) {
		encoder.writeFloat(vector.getX());
		encoder.writeFloat(vector.getY());
		encoder.writeFloat(vector.getZ());
	}

	public void writeVector2f(PacketEncoder encoder, Vector2f vector) {
		encoder.writeFloat(vector.getX());
		encoder.writeFloat(vector.getY());
	}

	public void writeBlockCoordinates(PacketEncoder encoder, Vector3f vector) {
		encoder.writeSignedVarInt(vector.getFloorX());
		encoder.writeUnsignedVarInt(vector.getFloorY());
		encoder.writeSignedVarInt(vector.getFloorZ());
	}

	public void writeWorldSettings(PacketEncoder encoder, WorldSettings settings) {
		encoder.writeSignedVarInt(settings.getSeed());
		encoder.writeSignedVarInt(settings.getGenerator());
		encoder.writeSignedVarInt(settings.getGameMode().ordinal());
		encoder.writeSignedVarInt(settings.getDifficulty().ordinal());
		writeBlockCoordinates(encoder, settings.getSpawn());
		encoder.writeBoolean(settings.hasAchievementsEnabled());
	}

	public void writeGameRules(PacketEncoder encoder, ArrayList<GameRule> gameRules) {
		encoder.writeUnsignedVarInt(gameRules.size());
		gameRules.forEach(gameRule -> {
			//encode game rules here
		});
	}

}
