package net.golem.block;

import net.golem.world.Position;

public abstract class Block extends Position {

	private BlockType type;

	public Block(BlockType type) {
		this.type = type;
	}

	public BlockType getType() {
		return type;
	}
}
