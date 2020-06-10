package net.golem.network.raknet.protocol;

public class SplitPacketInfo {

	public int splitId;

	public int splitIndex;

	public int splitCount;

	public SplitPacketInfo(int splitId, int splitIndex, int splitCount) {
		this.splitId = splitId;
		this.splitIndex = splitIndex;
		this.splitCount = splitCount;
	}

	public int getSplitId() {
		return splitId;
	}

	public int getSplitIndex() {
		return splitIndex;
	}

	public int getSplitCount() {
		return splitCount;
	}
}
