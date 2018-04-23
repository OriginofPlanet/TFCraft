package com.bioxx.tfc.Items.ItemBlocks;

import net.minecraft.block.Block;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.Blocks.IBlockStoneType;
import com.bioxx.tfc.api.Blocks.StoneType;
import com.bioxx.tfc.api.Constant.Global;

public class ItemStone extends ItemTerraBlock
{
	public ItemStone(Block block)
	{
		super(block);
		if (block instanceof IBlockStoneType) {
			metaNames = ((IBlockStoneType) block).getStoneType().getVariantNames().toArray(new String[0]);
		}
	}
}
