package com.bioxx.tfc.WorldGen.Generators;

import static com.bioxx.tfc.Core.TFC_Core.*;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Constant.Global;

public class WorldGenLiquidsTFC extends WorldGenerator
{
	private final Block liquidBlock;

	public WorldGenLiquidsTFC(Block block)
	{
		liquidBlock = block;
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (!isRawStone(world, i, j + 1, k) && !isRawStone(world, i, j - 1, k)) return false;
		if (!world.isAirBlock(i, j, k) && !isRawStone(world, i, j, k)) return false;

		int l = 0;
		int i1 = 0;
		for (ForgeDirection dir : Global.SIDES) {
			if (isRawStone(world, i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ)) l++;
			else if (world.isAirBlock(i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ)) i1++;
		}

		if (l == 3 && i1 == 1)
		{
			world.setBlock(i, j, k, liquidBlock, 0, 0x2);
			world.scheduledUpdatesAreImmediate = true;
			this.liquidBlock.updateTick(world, i, j, k, random);
			world.scheduledUpdatesAreImmediate = false;
		}
		return true;
	}
}
