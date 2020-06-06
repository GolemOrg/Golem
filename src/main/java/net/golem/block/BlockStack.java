package net.golem.block;

public class BlockStack {

	private Block block;

	private int count;

	public BlockStack(Block block) {
		this(block, 1);
	}

	public BlockStack(Block block, int count) {
		this.setBlock(block);
		this.setCount(count);
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
