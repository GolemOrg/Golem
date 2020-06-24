package net.golem.network.protocol;

import net.golem.Server;
import net.golem.math.Vector2f;
import net.golem.math.Vector3f;
import net.golem.network.GamePacketIds;
import net.golem.network.session.GameSessionAdapter;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;
import net.golem.types.Difficulty;
import net.golem.types.GameMode;
import net.golem.types.GameRule;
import net.golem.types.PlayerPermission;
import net.golem.world.WorldSettings;

import java.util.ArrayList;

public class StartGamePacket extends GamePacket {

	public long entityUniqueId;
	public long entityRuntimeId;

	public GameMode gamemode;

	public Vector3f spawn = new Vector3f(0, 0, 0);
	public Vector2f rotation = new Vector2f(0, 0);

	public WorldSettings settings = new WorldSettings("World", 0, GameMode.SURVIVAL, Difficulty.PEACEFUL);

	public PlayerPermission defaultPlayerPermission = PlayerPermission.MEMBER;

	public int chunkTickRadius = 4;

	public ArrayList<GameRule> gameRules = new ArrayList<>();

	public int enchantmentSeed = 0;

	public String multiplayerCorrelationId = "";

	public StartGamePacket() {
		super(GamePacketIds.START_GAME_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {
		encoder.writeSignedVarLong(entityUniqueId);
		encoder.writeUnsignedVarLong(entityRuntimeId);
		encoder.writeSignedVarInt(gamemode.getId());
		writeVector3f(encoder, spawn);
		writeVector2f(encoder, rotation);
		writeWorldSettings(encoder, settings);
		encoder.writeSignedVarInt(0); // day cycle stop time
		encoder.writeSignedVarInt(0); // EDU offer (0 = none, rest of world = 1, china = 2)
		encoder.writeBoolean(false); // edu edition features enabled
		encoder.writeFloat(settings.getRainLevel());
		encoder.writeFloat(settings.getLightningLevel());
		encoder.writeBoolean(false); // has confirmed platform locked content
		encoder.writeBoolean(true); // is multiplayer
		encoder.writeBoolean(true); // broadcast to LAN
		encoder.writeUnsignedVarInt(0); // xbox live broadcast mode
		encoder.writeUnsignedVarInt(0); // platform broadcast mode
		encoder.writeBoolean(true); // enable commands
		encoder.writeBoolean(false); // texture packs required / TODO: pack support

		writeGameRules(encoder, gameRules);

		encoder.writeBoolean(false); // bonus chest enabled
		encoder.writeBoolean(false); // start with map enabled

		encoder.writeUnsignedVarInt(defaultPlayerPermission.ordinal()); // default player permission
		encoder.writeIntLE(chunkTickRadius); // chunk tick radius
		encoder.writeBoolean(false); // locked behavior pack
		encoder.writeBoolean(false); // locked resource pack
		encoder.writeBoolean(false); // is from locked world template
		encoder.writeBoolean(false); // use msa gamertags only
		encoder.writeBoolean(false); // is from world template
		encoder.writeBoolean(false); // is world template option locked
		encoder.writeBoolean(false); // only spawn v1 villagers?

		encoder.writeString(Server.NETWORK_VERSION); // vanilla version

		encoder.writeString(settings.getName()); // level id
		encoder.writeString(settings.getName()); // world name
		encoder.writeString(""); // premium world template id

		encoder.writeBoolean(false); // is trial
		encoder.writeBoolean(true); // is movement server authoritative
		encoder.writeLongLE(settings.getCurrentTick()); // only used if trial mode is enabled

		encoder.writeUnsignedVarInt(enchantmentSeed);

		encoder.writeString(multiplayerCorrelationId);
	}

	@Override
	public void decode(PacketDecoder decoder) {
		// we don't decode this
	}

	@Override
	public boolean handle(GameSessionAdapter adapter) {
		return adapter.handle(this);
	}
}
