package net.golem.network.raknet.session.codec;

public interface CodecLayer {

	void update();

	void disconnect();

	void close();

}
