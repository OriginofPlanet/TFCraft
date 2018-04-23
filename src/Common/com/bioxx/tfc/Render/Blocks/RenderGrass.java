package com.bioxx.tfc.Render.Blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Blocks.SoilType;

public class RenderGrass
{
	public static boolean render(Block block, int x, int y, int z, RenderBlocks renderer)
	{
		float red = 1F;
		float green = 1F;
		float blue = 1F;

		int index = SoilType.getIndexIfAnyMatch(block, TFCBlocks.grasses, TFCBlocks.dryGrasses);
		if (index >= 0) renderer.renderStandardBlockWithAmbientOcclusion(TFCBlocks.dirts.getBlockAt(index), x, y, z, red, blue, green);

		renderer.renderStandardBlock(block, x, y, z);

		return true;
	}

	public static boolean renderClay(Block block, int x, int y, int z, RenderBlocks renderer)
	{
		float red = 1F;
		float green = 1F;
		float blue = 1F;

		int index = TFCBlocks.clayGrasses.getIndex(block);
		if (index >= 0) renderer.renderStandardBlockWithAmbientOcclusion(TFCBlocks.clays.getBlockAt(index), x, y, z, red, blue, green);

		renderer.renderStandardBlock(block, x, y, z);

		return true;
	}

	public static boolean renderPeat(Block block, int x, int y, int z, RenderBlocks renderer)
	{
		float red = 1F;
		float green = 1F;
		float blue = 1F;

		renderer.renderStandardBlockWithAmbientOcclusion(TFCBlocks.peat, x, y, z, red, blue, green);

		renderer.renderStandardBlock(block, x, y, z);

		return true;
	}
}
