package com.bioxx.tfc.Blocks.Terrain;

import com.bioxx.tfc.Blocks.Vanilla.BlockCustomWall;
import com.bioxx.tfc.api.Blocks.StoneType;

import net.minecraft.block.Block;

public class BlockConstructor implements StoneType.IBlockConstructor {

	public static final BlockConstructor INSTANCE = new BlockConstructor();

	private BlockConstructor() {}

	@Override
	public Block newCobble(StoneType stoneType) {
		return new BlockCobble(stoneType);
	}

	@Override
	public Block newStone(StoneType stoneType) {
		return new BlockStone(stoneType);
	}

	@Override
	public Block newSmooth(StoneType stoneType) {
		return new BlockSmooth(stoneType);
	}

	@Override
	public Block newBrick(StoneType stoneType) {
		return new BlockBrick(stoneType);
	}

	@Override
	public Block newWall(Block block, StoneType stoneType) {
		return new BlockCustomWall(block, stoneType);
	}

}
