package com.bioxx.tfc.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Blocks.Terrain.BlockCollapsible;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.TFCOptions;
import com.bioxx.tfc.api.Constant.Global;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BlockTerra extends Block
{
	private boolean isCollapsible = false;

	protected BlockTerra()
	{
		super(Material.rock);
	}

	protected BlockTerra(Material material)
	{
		super(material);
	}

	protected void setCollapsible(boolean isCollapsible) {
		this.isCollapsible = isCollapsible;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack is)
	{
		//TODO: Debug Message should go here if debug is toggled on
		if(TFCOptions.enableDebugMode && world.isRemote)
		{
			int metadata = world.getBlockMetadata(x, y, z);
			TerraFirmaCraft.LOG.info("Meta=" + (new StringBuilder()).append(getUnlocalizedName()).append(":").append(metadata).toString());
		}
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)  
	{
		if(TFCOptions.enableDebugMode && world.isRemote)
		{
			int metadata = world.getBlockMetadata(x, y, z);
			TerraFirmaCraft.LOG.info("Meta = " + (new StringBuilder()).append(getUnlocalizedName()).append(":").append(metadata).toString());
		}
		return false;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving)
	{
		onBlockPlacedBy(world, x, y, z, entityliving, null);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		super.harvestBlock(world, player, x, y, z, meta);
		TFC_Core.addPlayerExhaustion(player, 0.001f);
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
	{
		Block plant = plantable.getPlant(world, x, y + 1, z);
		if (plant == Blocks.cactus && this == Blocks.cactus)
		{
			return true;
		}

		if (plant == Blocks.reeds && this == Blocks.reeds)
		{
			return true;
		}

		EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
		switch (plantType)
		{
		case Cave:   return isSideSolid(world, x, y, z, UP);
		case Plains: return TFC_Core.isSoil(this) || TFC_Core.isFarmland(this);
		case Water:  return world.getBlock(x, y, z).getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0;
		case Beach:
			boolean isBeach = TFC_Core.isSand(this) || TFC_Core.isGravel(this);
			boolean hasWater = world.getBlock(x - 1, y, z    ).getMaterial() == Material.water ||
					world.getBlock(x + 1, y, z    ).getMaterial() == Material.water ||
					world.getBlock(x,     y, z - 1).getMaterial() == Material.water ||
					world.getBlock(x,     y, z + 1).getMaterial() == Material.water;
			return isBeach && hasWater;
		default: return false;
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (isCollapsible && !world.isRemote && world.doChunksNearChunkExist(i, j, k, 1) && !BlockCollapsible.isNearSupport(world, i, j, k, 4, 0)) {
			int meta = world.getBlockMetadata(i, j, k);

			boolean canFallOneBelow = BlockCollapsible.canFallBelow(world, i, j-1, k);
			byte count = 0;
			List<ForgeDirection> sides = new ArrayList<>();

			for (ForgeDirection dir : Global.SIDES) if (world.isAirBlock(i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ)) {
				count++;
				if (BlockCollapsible.canFallBelow(world, i + dir.offsetX, j + dir.offsetY - 1, k + dir.offsetZ)) sides.add(dir);
			}

			if (!canFallOneBelow && count > 2 && !sides.isEmpty()) {
				ForgeDirection dir = sides.get(random.nextInt(sides.size()));
				world.setBlockToAir(i, j, k);
				world.setBlock(i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ, this, meta, 0x2);
				BlockCollapsible.tryToFall(world, i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ, this);
			} else if(canFallOneBelow) {
				BlockCollapsible.tryToFall(world, i, j, k, this);
			}
		}
	}
}
