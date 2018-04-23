package com.bioxx.tfc.Blocks.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Blocks.BlockTerra;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.api.Blocks.IBlockSoil;
import com.bioxx.tfc.api.Blocks.StoneVariant;
import com.bioxx.tfc.api.Constant.Global;

public class BlockSand extends BlockTerra implements IBlockSoil
{
	protected IIcon[] icons = new IIcon[16];
	protected int index;

	public BlockSand(int index)
	{
		super(Material.sand);
		this.setCreativeTab(TFCTabs.TFC_BUILDING);
		this.index = index;
		this.setCollapsible(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		// Change to false if this block should not be added to the creative tab
		Boolean addToCreative = true;

		if(addToCreative)
		{
			for (int i = 0; i < 16; i++) if (StoneVariant.get(i + index).isAvailable()) {
				list.add(new ItemStack(item, 1, i));
			}
		}
	}

	@Override
	public int damageDropped(int dmg)
	{
		return dmg;
	}

	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	@Override
	public IIcon getIcon(IBlockAccess bAccess, int x, int y, int z, int side)
	{
		int meta = bAccess.getBlockMetadata(x, y, z);
		if(meta >= icons.length) return icons[icons.length - 1];
		return icons[meta];
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	@Override
	public IIcon getIcon(int side, int meta)
	{
		if(meta >= icons.length) return icons[icons.length - 1];
		return icons[meta];
	}

	@Override
	public void registerBlockIcons(IIconRegister registerer)
	{
		StoneVariant sv;
		for (int i = 0; i < 16; i++) if ((sv = StoneVariant.get(i + index)).isAvailable()) {
			icons[i] = registerer.registerIcon(Reference.MOD_ID + ":" + "sand/Sand " + sv.getName());
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if(!world.isRemote)
		{
			BlockCollapsible.tryToFall(world, x, y, z, this);
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}
	}

	public int getStoneTypeIndex() {
		return index;
	}
}
