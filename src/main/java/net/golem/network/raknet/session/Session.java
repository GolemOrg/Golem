package net.golem.network.raknet.session;


import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.protocol.RakNetPacket;
import net.golem.network.raknet.types.PacketReliability;

public interface Session {

	void close(String reason);

	void close();

	int getMaximumTransferUnits();

	void setMaximumTransferUnits(int size);

	void update();

	void handle(DataPacket packet);

	void sendPacket(DataPacket packet, boolean direct);

	void sendPacket(DataPacket packet);

	void sendPacket(RakNetPacket packet, PacketReliability reliability);

	void sendPacket(RakNetPacket packet, PacketReliability reliability, boolean immediate);

	void addListener(SessionListener listener);
}
