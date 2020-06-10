package net.golem.network.raknet.protocol;

import lombok.extern.log4j.Log4j2;
import net.golem.network.raknet.DataPacket;

@Log4j2
public abstract class RakNetPacket extends DataPacket  {

	public static final byte[] MAGIC = new byte[] { (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };

	public RakNetPacket(int id) {
		super(id);
	}

}
