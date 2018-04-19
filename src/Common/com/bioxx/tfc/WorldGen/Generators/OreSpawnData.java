package com.bioxx.tfc.WorldGen.Generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;

import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Blocks.StoneType;
import com.bioxx.tfc.api.Blocks.StoneVariant;
import com.bioxx.tfc.api.Constant.Global;

public class OreSpawnData
{
	public int type, size, meta, rarity, min = 5, max = 128, vDensity, hDensity;
	public Block block;
	public Map<Block, List<Integer>> base;

	public OreSpawnData(String t, String s, String blockName, int m, int r, String[] baseRocks)
	{
		block = Block.getBlockFromName(blockName);

		if (block == null)
		{
			TerraFirmaCraft.LOG.error(TFC_Core.translate("error.error") + " " + TFC_Core.translate("error.OreCFG") + " " + blockName);
			throw new java.lang.NullPointerException(TFC_Core.translate("error.OreCFG") + " " + blockName);
		}

		meta = m;
		rarity = r;
		if ("default".equals(t))
			type = 0;
		else
			type = 1;

		if ("small".equals(s))
			size = 0;
		else if ("medium".equals(s))
			size = 1;
		else
			size = 2;

		base = new HashMap<Block, List<Integer>>();
		for (String name : baseRocks)
		{
			getOre(name);
		}
	}

	public OreSpawnData(String t, String s, String blockName, int m, int r, String[] baseRocks, int minHeight, int maxHeight, int v, int h)
	{
		this(t, s, blockName, m, r, baseRocks);
		min = minHeight;
		max = maxHeight;
		vDensity = v;
		hDensity = h;
	}

	private void getOre(String name)
	{
		for (StoneVariant sv : StoneVariant.getAllVariants()) {
			if (name.equalsIgnoreCase(sv.getName())) {
				base.computeIfAbsent(sv.getType().getStone(), ($) -> new ArrayList<>()).add(sv.getLocalIndex());
				return;
			}
		}
		for (StoneType st : StoneType.getAllTypes()) {
			if (name.equalsIgnoreCase(st.getName())) {
				base.computeIfAbsent(st.getStone(), ($) -> new ArrayList<>()).add(-1);
				return;
			}
		}
	}
}