package net.golem.network.raknet.session;


import net.golem.network.raknet.DataPacket;
import net.golem.network.raknet.types.PacketReliability;

public interface Session {

	void close(String reason);

	void close();

	int getMaximumTransferUnits();

	void setMaximumTransferUnits(int size);

	void tick();

	void handle(DataPacket packet);

	void sendPacket(DataPacket packet, PacketReliability reliability, boolean immediate);

	void sendPacket(DataPacket packet, boolean immediate);

	void sendPacket(DataPacket packet);


	void addListener(SessionListener listener);
}
