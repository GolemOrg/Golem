package net.golem.network.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.network.session.GameSessionAdapter;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Log4j2
@ToString(exclude = "clientData")
public class LoginPacket extends GamePacket {

	public int protocol;

	public String username;
	public String xuid;
	public UUID clientUUID;

	public JsonObject extraData;

	public JsonObject clientData;

	public LoginPacket() {
		super(GamePacketIds.LOGIN_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {

	}

	@Override
	public void decode(PacketDecoder decoder) {
		protocol = decoder.readInt();
		PacketDecoder loginDecoder = new PacketDecoder(decoder.readSlice(decoder.readUnsignedVarInt()));
		decodeChainData(loginDecoder);
		decodeClientData(loginDecoder);
	}

	private void decodeChainData(PacketDecoder loginDecoder) {
		String data = loginDecoder.readSlice(loginDecoder.readIntLE()).toString(StandardCharsets.UTF_8);
		Map<String, List<String>> map = new Gson().fromJson(data, new TypeToken<Map<String, List<String>>>(){}.getType());
		if(map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;
		List<String> chains = map.get("chain");
		chains.forEach(chainToken -> {
			JsonObject chain = decodeToken(chainToken);
			if(chain == null || !chain.has("extraData")) {
				// TODO: Handle other chains
				return;
			}
			extraData = chain.get("extraData").getAsJsonObject();
			username = extraData.get("displayName").getAsString();
			clientUUID = UUID.fromString(extraData.get("identity").getAsString());
			xuid = extraData.get("XUID").getAsString();
		});
	}

	private void decodeClientData(PacketDecoder loginDecoder) {
		clientData = decodeToken(loginDecoder.readSlice(loginDecoder.readIntLE()).toString(StandardCharsets.UTF_8));
	}

	private JsonObject decodeToken(String token) {
		String[] base = token.split("\\.");
		if (base.length < 2) return null;
		return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
	}

	@Override
	public boolean handle(GameSessionAdapter adapter) {
		try {
			return adapter.handle(this);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
