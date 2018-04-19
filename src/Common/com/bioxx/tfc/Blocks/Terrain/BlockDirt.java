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

public class BlockDirt extends BlockTerra implements IBlockSoil
{
	protected IIcon[] icons = new IIcon[16];
	protected int index;

	public BlockDirt(int index)
	{
		super(Material.ground);
		this.setCreativeTab(TFCTabs.TFC_BUILDING);
		this.index = index;
		this.setTickRandomly(true);
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

	@Override
	public Item getItemDropped(int metadata, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	@Override
	public IIcon getIcon(IBlockAccess bAccess, int x, int y, int z, int side)
	{
		int meta = bAccess.getBlockMetadata(x, y, z);
		if(meta >= icons.length) return icons[icons.length - 1];
		return icons[meta];
	}

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
			icons[i] = registerer.registerIcon(Reference.MOD_ID + ":" + "soil/Dirt " + sv.getName());
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public int tickRate(World world)
	{
		return 3;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b)
	{
		if (!world.isRemote)
		{
			BlockCollapsible.tryToFall(world, x, y, z, this);
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}
	}

	@Override
	public int getStoneTypeIndex() {
		return index;
	}
}
