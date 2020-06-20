package net.golem.network.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import net.golem.network.GamePacketIds;
import net.golem.raknet.protocol.DataPacket;
import net.golem.raknet.codec.PacketDecoder;
import net.golem.raknet.codec.PacketEncoder;

@Log4j2
public class LoginPacket extends DataPacket {

	public String username;

	public int protocol;

	public String clientUUID;

	public int clientId;

	public String xuid;

	public String identityPublicKey;

	public String serverAddress;

	public String locale;

	public String[][] chainData;

	public String clientDataJwt;

	public Object[] clientData;

	public boolean skipVerification = false;

	public LoginPacket() {
		super(GamePacketIds.LOGIN_PACKET);
	}

	@Override
	public void encode(PacketEncoder encoder) {

	}

	@Override
	public void decode(PacketDecoder decoder) {
		log.info("Received login packet");
		protocol = decoder.readInt();
		log.info("Protocol version: {}", protocol);
	}
}
