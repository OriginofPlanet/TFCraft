package com.bioxx.tfc.Items.ItemBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Items.ItemFlatGeneric;
import com.bioxx.tfc.api.Blocks.StoneVariant;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemFlatRock extends ItemFlatGeneric {

	public ItemFlatRock() {
		this.hasSubtypes = true;
		this.metaNames = Arrays.stream(StoneVariant.getVariants()).map(StoneVariant::getName).collect(Collectors.toList()).toArray(new String[0]);
		icons = new IIcon[this.metaNames.length];
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		StoneVariant.getAllVariants().forEach(sv -> list.add(new ItemStack(this, 1, sv.getGeneralIndex())));
	}

	@Override
	public void registerIcons(IIconRegister registerer) {
		StoneVariant.getAllVariants().forEach(sv -> icons[sv.getGeneralIndex()] = registerer.registerIcon(Reference.MOD_ID + ":" + this.textureFolder + sv.getName()));
	}

}
