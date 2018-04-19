package com.bioxx.tfc.Blocks.Terrain;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.api.Blocks.StoneType;

import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockBrick extends BlockSmooth {

	public BlockBrick(StoneType stoneType) {
		super(stoneType);
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegisterer)
	{
		stoneType.getVariants().forEach(sv -> {icons[sv.getLocalIndex()] = iconRegisterer.registerIcon(Reference.MOD_ID + ":" + "rocks/"+sv.getName()+" Brick");});
	}

}
