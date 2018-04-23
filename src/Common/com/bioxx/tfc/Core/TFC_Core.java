package com.bioxx.tfc.Core;

import static com.bioxx.tfc.api.TFCBlocks.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.storage.WorldInfo;

import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Chunkdata.ChunkData;
import com.bioxx.tfc.Chunkdata.ChunkDataManager;
import com.bioxx.tfc.Core.Player.BodyTempStats;
import com.bioxx.tfc.Core.Player.FoodStatsTFC;
import com.bioxx.tfc.Core.Player.InventoryPlayerTFC;
import com.bioxx.tfc.Core.Player.SkillStats;
import com.bioxx.tfc.Food.ItemFoodTFC;
import com.bioxx.tfc.Items.ItemOre;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.TileEntities.TEMetalSheet;
import com.bioxx.tfc.WorldGen.TFCBiome;
import com.bioxx.tfc.api.*;
import com.bioxx.tfc.api.Blocks.SoilType;
import com.bioxx.tfc.api.Blocks.StoneType;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Entities.IAnimal;
import com.bioxx.tfc.api.Enums.EnumFuelMaterial;
import com.bioxx.tfc.api.Interfaces.IFood;

public class TFC_Core
{
	private static Map<Integer, ChunkDataManager> cdmMap = new HashMap<Integer, ChunkDataManager>();
	public static boolean preventEntityDataUpdate;

	public static ChunkDataManager getCDM(World world)
	{
		int key = world.isRemote ? 128 | world.provider.dimensionId : world.provider.dimensionId;
		return cdmMap.get(key);
	}

	public static ChunkDataManager addCDM(World world)
	{
		int key = world.isRemote ? 128 | world.provider.dimensionId : world.provider.dimensionId;
		if(!cdmMap.containsKey(key))
			return cdmMap.put(key, new ChunkDataManager(world));
		else return cdmMap.get(key);
	}

	public static ChunkDataManager removeCDM(World world)
	{
		int key = world.isRemote ? 128 | world.provider.dimensionId : world.provider.dimensionId;
		return cdmMap.remove(key);
	}

	@SideOnly(Side.CLIENT)
	public static int getMouseX()
	{
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		int i = scaledresolution.getScaledWidth();
		int k = Mouse.getX() * i / Minecraft.getMinecraft().displayWidth;

		return k;
	}

	@SideOnly(Side.CLIENT)
	public static int getMouseY()
	{
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		int j = scaledresolution.getScaledHeight();
		int l = j - Mouse.getY() * j / Minecraft.getMinecraft().displayHeight - 1;

		return l;
	}

	public static Boolean isBlockAboveSolid(IBlockAccess blockAccess, int i, int j, int k)
	{
		if(TerraFirmaCraft.proxy.getCurrentWorld().getBlock(i, j + 1, k).isOpaqueCube())
			return true;
		return false;
	}

	public static int getExtraEquipInventorySize(){
		//Just the back
		return 1;
	}

	public static InventoryPlayer getNewInventory(EntityPlayer player)
	{
		InventoryPlayer ip = player.inventory;
		NBTTagList nbt = new NBTTagList();
		nbt = player.inventory.writeToNBT(nbt);
		ip = new InventoryPlayerTFC(player);
		ip.readFromNBT(nbt);
		return ip;
	}

	public static ItemStack randomGem(Random random, int rockType) //TODO GT gem?
	{
		for (int i = 0, k = 500; i < 5; i++, k *= 2) if (random.nextInt(k) == 0) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			items.add(new ItemStack(TFCItems.gemAgate, 1, i));
			items.add(new ItemStack(TFCItems.gemAmethyst, 1, i));
			items.add(new ItemStack(TFCItems.gemBeryl, 1, i));
			items.add(new ItemStack(TFCItems.gemEmerald, 1, i));
			items.add(new ItemStack(TFCItems.gemGarnet, 1, i));
			items.add(new ItemStack(TFCItems.gemJade, 1, i));
			items.add(new ItemStack(TFCItems.gemJasper, 1, i));
			items.add(new ItemStack(TFCItems.gemOpal, 1, i));
			items.add(new ItemStack(TFCItems.gemRuby, 1, i));
			items.add(new ItemStack(TFCItems.gemSapphire, 1, i));
			items.add(new ItemStack(TFCItems.gemTourmaline, 1, i));
			items.add(new ItemStack(TFCItems.gemTopaz, 1, i));

			return (ItemStack) items.toArray()[random.nextInt(items.toArray().length)];
		}
		return null;
	}

	public static void surroundWithLeaves(World world, int i, int j, int k, int meta, Random r)
	{
		for (int y = 2; y >= -2; y--)
		{
			for (int x = 2; x >= -2; x--)
			{
				for (int z = 2; z >= -2; z--)
				{
					if(world.isAirBlock(i + x, j + y, k + z))
						world.setBlock(i + x, j + y, k + z, leaves, meta, 2);
				}
			}
		}
	}

	public static void setupWorld(World world)
	{
		long seed = world.getSeed();
		Random r = new Random(seed);
		world.provider.registerWorld(world);
		Recipes.registerAnvilRecipes(r, world);
		TFC_Time.updateTime(world);
		// TerraFirmaCraft.proxy.registerSkyProvider(world.provider);
	}

	public static void setupWorld(World w, long seed)
	{
		try
		{
			// ReflectionHelper.setPrivateValue(WorldInfo.class,
			// w.getWorldInfo(), "randomSeed", seed);
			ReflectionHelper.setPrivateValue(WorldInfo.class, w.getWorldInfo(), seed, 0);
			setupWorld(w);
		}
		catch (Exception ex)
		{
		}
	}

	public static boolean isRawStone(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		return StoneType.getAllTypes().stream().map(StoneType::getStone).anyMatch(b -> b == block);
	}

	public static boolean isSmoothStone(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		return StoneType.getAllTypes().stream().map(StoneType::getSmooth).anyMatch(b -> b == block);
	}

	public static boolean isSmoothStone(Block block)
	{
		return StoneType.getAllTypes().stream().map(StoneType::getSmooth).anyMatch(b -> b == block);
	}

	public static boolean isBrickStone(Block block)
	{
		return StoneType.getAllTypes().stream().map(StoneType::getBrick).anyMatch(b -> b == block);
	}

	public static boolean isRawStone(Block block)
	{
		return StoneType.getAllTypes().stream().map(StoneType::getStone).anyMatch(b -> b == block);
	}

	public static boolean isOreStone(Block block)
	{
		return block == ore
				|| block == ore2
				|| block == ore3;
	}

	public static boolean isNaturalStone(Block block)
	{
		return isRawStone( block ) || isOreStone( block );
	}

	public static boolean isCobbleStone(Block block)
	{
		return StoneType.getAllTypes().stream().map(StoneType::getCobble).anyMatch(b -> b == block);
	}

	public static boolean isStoneIgEx(Block block)
	{
		return stoneTypeIgEx.getAllBlocks().stream().anyMatch(b -> b == block);
	}

	public static boolean isStoneIgIn(Block block)
	{
		return stoneTypeIgIn.getAllBlocks().stream().anyMatch(b -> b == block);
	}

	public static boolean isStoneSed(Block block)
	{
		return stoneTypeSed.getAllBlocks().stream().anyMatch(b -> b == block);
	}

	public static boolean isStoneMM(Block block)
	{
		return stoneTypeMM.getAllBlocks().stream().anyMatch(b -> b == block);
	}

	public static boolean isDirt(Block block)
	{
		return dirts.isInstance(block);
	}

	public static boolean isGrassOrDirt(Block block) {
		return grasses.isInstance(block) || dirts.isInstance(block);
	}

	public static boolean isFarmland(Block block)
	{
		return tilledSoils.isInstance(block);
	}

	public static boolean isGrass(Block block)
	{
		return grasses.isInstance(block)
				|| clayGrasses.isInstance(block)
				|| block == peatGrass
				|| dryGrasses.isInstance(block);
	}

	public static boolean isGrassNormal(Block block)
	{
		return grasses.isInstance(block);
	}


	public static boolean isLushGrass(Block block)
	{
		return grasses.isInstance(block)
				|| clayGrasses.isInstance(block)
				|| block == peatGrass;
	}

	public static boolean isClayGrass(Block block)
	{
		return clayGrasses.isInstance(block);
	}

	public static boolean isPeatGrass(Block block)
	{
		return block == peatGrass;
	}

	public static boolean isDryGrass(Block block)
	{
		return dryGrasses.isInstance(block);
	}

	@Deprecated
	public static boolean isGrassType1(Block block)
	{
		return block == grasses.getBlockAt(0)
				|| block == clayGrasses.getBlockAt(0)
				|| block == dryGrasses.getBlockAt(0);
	}

	@Deprecated
	public static boolean isGrassType2(Block block)
	{
		return block == grasses.getBlockAt(1)
				|| block == clayGrasses.getBlockAt(1)
				|| block == dryGrasses.getBlockAt(1);
	}

	public static boolean isClay(Block block)
	{
		return clays.isInstance(block);
	}

	public static boolean isSand(Block block)
	{
		return sands.isInstance(block);
	}

	public static boolean isPeat(Block block)
	{
		return block == peat;
	}

	public static boolean isHotWater(Block block)
	{
		return block == hotWater || block == hotWaterStationary;
	}

	public static boolean isWater(Block block)
	{
		return isSaltWater(block)
				|| isFreshWater(block);
	}

	public static boolean isWaterFlowing(Block block)
	{
		return block == saltWater || block == freshWater;
	}

	public static boolean isSaltWater(Block block)
	{
		return block == saltWater || block == saltWaterStationary;
	}

	public static boolean isSaltWaterIncludeIce(Block block, int meta, Material mat)
	{
		return block == saltWater || block == saltWaterStationary
				|| mat == Material.ice && meta == 0;
	}

	public static boolean isFreshWater(Block block)
	{
		return block == freshWater || block == freshWaterStationary;
	}

	public static boolean isFreshWaterIncludeIce(Block block, int meta)
	{
		return block == freshWater || block == freshWaterStationary
				|| block == ice && meta != 0;
	}

	public static boolean isFreshWaterIncludeIce(Block block, int meta, Material mat)
	{
		return block == freshWater || block == freshWaterStationary
				|| mat == Material.ice && meta != 0;
	}

	public static boolean isSoil(Block block)
	{
		return isGrass(block)
				|| isDirt(block)
				|| isClay(block)
				|| isPeat(block);
	}

	public static boolean isSoilOrGravel(Block block)
	{
		return isGrass(block)
				|| isDirt(block)
				|| isClay(block)
				|| isPeat(block)
				|| isGravel(block);
	}

	public static boolean isGravel(Block block)
	{
		return gravels.isInstance(block);
	}

	public static boolean isGround(Block block)
	{
		return 	   isSoilOrGravel(block)
				|| isRawStone(block)
				|| isSand(block);
	}

	@Deprecated
	public static boolean isGroundType1(Block block)
	{
		return isGrassType1(block) || block == dirts.getBlockAt(0) || block == gravels.getBlockAt(0) || block == sands.getBlockAt(0);
	}

	public static boolean isSoilWAILA(Block block)
	{
		return isDirt(block) || isGravel(block) || isSand(block) || isGrassNormal(block) || isDryGrass(block);
	}

	public static int getSoilMetaFromStone(Block inBlock, int inMeta)
	{
		for (StoneType st : StoneType.getAllTypes()) if (inBlock == st.getStone()) return st.get(inMeta).getGeneralIndex();
		return 0;
	}

	public static int getSoilMeta( int inMeta)
	{
		return inMeta & 15;
	}

	public static int getItemMetaFromStone(Block inBlock, int inMeta)
	{
		for (StoneType st : StoneType.getAllTypes()) if (inBlock == st.getStone()) return st.get(inMeta).getGeneralIndex();
		return 0;
	}

	public static Block getTypeForGrassWithRain(int inMeta, float rain)
	{
		if (rain >= 500)
			return getTypeForGrass(inMeta);
		return getTypeForDryGrass(inMeta);
	}

	public static Block getTypeForGrassWithRainByBlock(Block block, float rain)
	{
		if(rain >= 500)
			return getTypeForGrassFromSoil(block);
		return getTypeForDryGrassFromSoil(block);
	}

	public static Block getTypeForGrass(int inMeta)
	{
		return grasses.getBlock(inMeta);
	}

	public static Block getTypeForGrassFromDirt(Block block)
	{
		return grasses.getBlockAt(dirts.getIndex(block));
	}

	public static Block getTypeForDryGrass(int inMeta)
	{
		return dryGrasses.getBlock(inMeta);
	}

	public static Block getTypeForDryGrassFromSoil(Block block)
	{
		if (dryGrasses.isInstance(block)) return block;
		return dryGrasses.getBlockAt(SoilType.getIndexIfAnyMatch(block, grasses, dirts));
	}

	public static Block getTypeForGrassFromSoil(Block block)
	{
		if (grasses.isInstance(block)) return block;
		return grasses.getBlockAt(SoilType.getIndexIfAnyMatch(block, dryGrasses, dirts));
	}

	public static Block getTypeForClayGrass(int inMeta)
	{
		return clayGrasses.getBlock(inMeta);
	}

	public static Block getTypeForClayGrass(Block block)
	{
		if (clayGrasses.isInstance(block)) return block;
		return clayGrasses.getBlockAt(SoilType.getIndexIfAnyMatch(block, grasses, dryGrasses, dirts, gravels, sands));
	}

	public static Block getTypeForDirt(int inMeta)
	{
		return dirts.getBlock(inMeta);
	}

	public static Block getTypeForDirtFromGrass(Block block)
	{
		if (dirts.isInstance(block)) return block;
		return dirts.getBlockAt(SoilType.getIndexIfAnyMatch(block, grasses, dryGrasses));
	}

	public static Block getTypeForSoil(Block block)
	{
		if (TFC_Core.isGrass(block))
		{
			if (isPeatGrass(block)) return peat;
			return dirts.getBlockAt(SoilType.getIndexIfAnyMatch(block, grasses, dryGrasses, clayGrasses));
		}

		return block;
	}

	public static Block getTypeForClay(int inMeta)
	{
		return clays.getBlock(inMeta);
	}

	public static Block getTypeForClay(Block block)
	{
		return clays.getBlockAt(SoilType.getIndexIfAnyMatch(block, grasses, dryGrasses, clayGrasses, dirts, sands, gravels));
	}

	public static Block getTypeForSand(int inMeta)
	{
		return sands.getBlock(inMeta);
	}

	public static Block getTypeForGravel(int inMeta)
	{
		return gravels.getBlock(inMeta);
	}

	public static int getRockLayerFromHeight(World world, int x, int y, int z)
	{
		ChunkData cd = TFC_Core.getCDM(world).getData(x >> 4, z >> 4);
		if (cd != null)
		{
			int[] hm = cd.heightmap;
			int localX = x & 15;
			int localZ = z & 15;
			int localY = localX + localZ * 16;
			if (y <= TFCOptions.rockLayer3Height + hm[localY])
				return 2;
			else if (y <= TFCOptions.rockLayer2Height + hm[localY])
				return 1;
			else
				return 0;
		}
		return 0;
	}

	public static boolean convertGrassToDirt(World world, int i, int j, int k)
	{
		Block block = world.getBlock(i, j, k);
		int meta = world.getBlockMetadata(i, j, k);
		if(TFC_Core.isGrass(block))
		{
			world.setBlock(i, j, k, getTypeForDirtFromGrass(block), meta, 2);
			return true;
		}
		return false;
	}

	public static EnumFuelMaterial getFuelMaterial(ItemStack is)
	{
		if(is.getItem() == Item.getItemFromBlock(peat))
			return EnumFuelMaterial.PEAT;
		if(is.getItem() == TFCItems.coal && is.getItemDamage() == 0)
			return EnumFuelMaterial.COAL;
		else if(is.getItem() == TFCItems.coal && is.getItemDamage() == 1)
			return EnumFuelMaterial.CHARCOAL;
		if(is.getItemDamage() == 0)
			return EnumFuelMaterial.ASH;
		else if (is.getItemDamage() == 1)
			return EnumFuelMaterial.ASPEN;
		else if (is.getItemDamage() == 2)
			return EnumFuelMaterial.BIRCH;
		else if (is.getItemDamage() == 3)
			return EnumFuelMaterial.CHESTNUT;
		else if (is.getItemDamage() == 4)
			return EnumFuelMaterial.DOUGLASFIR;
		else if (is.getItemDamage() == 5)
			return EnumFuelMaterial.HICKORY;
		else if (is.getItemDamage() == 6)
			return EnumFuelMaterial.MAPLE;
		else if (is.getItemDamage() == 7)
			return EnumFuelMaterial.OAK;
		else if (is.getItemDamage() == 8)
			return EnumFuelMaterial.PINE;
		else if (is.getItemDamage() == 9)
			return EnumFuelMaterial.REDWOOD;
		else if (is.getItemDamage() == 10)
			return EnumFuelMaterial.SPRUCE;
		else if (is.getItemDamage() == 11)
			return EnumFuelMaterial.SYCAMORE;
		else if (is.getItemDamage() == 12)
			return EnumFuelMaterial.WHITECEDAR;
		else if (is.getItemDamage() == 13)
			return EnumFuelMaterial.WHITEELM;
		else if (is.getItemDamage() == 14)
			return EnumFuelMaterial.WILLOW;
		else if (is.getItemDamage() == 15)
			return EnumFuelMaterial.KAPOK;
		else if(is.getItemDamage() == 16)
			return EnumFuelMaterial.ACACIA;
		return EnumFuelMaterial.ASPEN;
	}

	public static boolean showShiftInformation()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	public static boolean showCtrlInformation()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
	}

	public static FoodStatsTFC getPlayerFoodStats(EntityPlayer player)
	{
		FoodStatsTFC foodstats = new FoodStatsTFC(player);
		foodstats.readNBT(player.getEntityData());
		return foodstats;
	}

	public static void setPlayerFoodStats(EntityPlayer player, FoodStatsTFC foodstats)
	{
		foodstats.writeNBT(player.getEntityData());
	}

	public static BodyTempStats getBodyTempStats(EntityPlayer player)
	{
		BodyTempStats body = new BodyTempStats();
		body.readNBT(player.getEntityData());
		return body;
	}

	public static void setBodyTempStats(EntityPlayer player, BodyTempStats tempStats)
	{
		tempStats.writeNBT(player.getEntityData());
	}

	public static SkillStats getSkillStats(EntityPlayer player)
	{
		SkillStats skills = new SkillStats(player);
		skills.readNBT(player.getEntityData());
		return skills;
	}

	public static void setSkillStats(EntityPlayer player, SkillStats skills)
	{
		skills.writeNBT(player.getEntityData());
	}

	public static boolean isTopFaceSolid(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z).isNormalCube())
			return true;
		else if(world.getBlock(x, y, z) == metalSheet)
		{
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(x, y, z);
			if(te.topExists())
				return true;
		}
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, ForgeDirection.UP);
	}

	public static boolean isBottomFaceSolid(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z).isNormalCube())
			return true;
		else if(world.getBlock(x, y, z) == metalSheet)
		{
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(x, y, z);
			if(te.bottomExists())
				return true;
		}
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, ForgeDirection.DOWN);
	}

	public static boolean isNorthFaceSolid(World world, int x, int y, int z)
	{
		Block bid = world.getBlock(x, y, z);
		if(bid.isNormalCube())
			return true;
		else if(bid == metalSheet)
		{
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(x, y, z);
			if(te.northExists())
				return true;
		}
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, ForgeDirection.NORTH);
	}

	public static boolean isSouthFaceSolid(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z).isNormalCube())
			return true;
		else if(world.getBlock(x, y, z) == metalSheet)
		{
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(x, y, z);
			if(te.southExists())
				return true;
		}
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, ForgeDirection.SOUTH);
	}

	public static boolean isEastFaceSolid(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z).isNormalCube())
			return true;
		else if(world.getBlock(x, y, z) == metalSheet)
		{
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(x, y, z);
			if(te.eastExists())
				return true;
		}
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, ForgeDirection.EAST);
	}

	public static boolean isWestFaceSolid(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z).isNormalCube())
			return true;
		else if(world.getBlock(x, y, z) == metalSheet)
		{
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(x, y, z);
			if(te.westExists())
				return true;
		}
		return world.getBlock(x, y, z).isSideSolid(world, x, y, z, ForgeDirection.WEST);
	}

	public static boolean isSurroundedSolid(World world, int x, int y, int z)
	{
		return TFC_Core.isNorthFaceSolid(world, x, y, z + 1) &&
				TFC_Core.isSouthFaceSolid(world, x, y, z - 1) &&
				TFC_Core.isEastFaceSolid(world, x - 1, y, z) &&
				TFC_Core.isWestFaceSolid(world, x + 1, y, z) &&
				TFC_Core.isTopFaceSolid(world, x, y - 1, z);
	}

	public static boolean isSurroundedStone(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z + 1).getMaterial() == Material.rock &&
				world.getBlock(x, y, z - 1).getMaterial() == Material.rock &&
				world.getBlock(x - 1, y, z).getMaterial() == Material.rock &&
				world.getBlock(x + 1, y, z).getMaterial() == Material.rock &&
				world.getBlock(x, y - 1, z).getMaterial() == Material.rock;
	}

	public static boolean isOreIron(ItemStack is)
	{
		return is.getItem() instanceof ItemOre && ((ItemOre) is.getItem()).getMetalType(is) == Global.PIGIRON;
	}

	public static float getEntityMaxHealth(EntityLivingBase entity)
	{
		return (float) entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
	}

	public static float getPercentGrown(IAnimal animal)
	{
		float birth = animal.getBirthDay();
		float time = TFC_Time.getTotalDays();
		float percent = (time - birth) / animal.getNumberOfDaysToAdult();
		return Math.min(percent, 1f);
	}

	public static void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}

	public static boolean isPlayerInDebugMode(EntityPlayer player)
	{
		return TFCOptions.enableDebugMode;
	}

	/**
	 * Adds exhaustion to the player. 0.001 is a standard amount.
	 */
	public static void addPlayerExhaustion(EntityPlayer player, float exhaustion)
	{
		FoodStatsTFC foodstats = TFC_Core.getPlayerFoodStats(player);
		foodstats.addFoodExhaustion(exhaustion);
		//foodstats.addWaterExhaustion(exhaustion);
		TFC_Core.setPlayerFoodStats(player, foodstats);
	}

	public static float getEnvironmentalDecay(float temp)
	{
		if (temp > 0)
		{
			float tempFactor = 1f - (15f / (15f + temp));
			return tempFactor * 2;
		}
		else
			return 0;
	}

	/**
	 * This is the default item ticking method for use by all containers. Call
	 * this if you don't want to do custom environmental decay math.
	 */
	public static void handleItemTicking(IInventory iinv, World world, int x, int y, int z)
	{
		handleItemTicking(iinv, world, x, y, z, 1);
	}

	/**
	 * This is the default item ticking method for use by all containers. Call
	 * this if you don't want to do custom environmental decay math.
	 */
	public static void handleItemTicking(ItemStack[] iinv, World world, int x, int y, int z)
	{
		handleItemTicking(iinv, world, x, y, z, 1);
	}

	/**
	 * This version of the method assumes that the environmental decay modifier
	 * has already been calculated.
	 */
	public static void handleItemTicking(IInventory iinv, World world, int x, int y, int z, float environmentalDecayFactor)
	{
		for (int i = 0; !world.isRemote && i < iinv.getSizeInventory(); i++)
		{
			ItemStack is = iinv.getStackInSlot(i);
			if (is != null && iinv.getStackInSlot(i).stackSize <= 0)
				iinv.setInventorySlotContents(i, null);

			if (is != null)
			{
				if(is.stackSize == 0)
				{
					iinv.setInventorySlotContents(i, null);
					continue;
				}
				if (is.getItem() instanceof ItemTerra && ((ItemTerra) is.getItem()).onUpdate(is, world, x, y, z))
					continue;
				else if (is.getItem() instanceof ItemTerraBlock && ((ItemTerraBlock) is.getItem()).onUpdate(is, world, x, y, z))
					continue;
				is = tickDecay(is, world, x, y, z, environmentalDecayFactor, 1f);
				if(is != null)
					TFC_ItemHeat.handleItemHeat(is);
				iinv.setInventorySlotContents(i, is);
			}

		}
	}

	//Takes a small float in the range of 0.5 to 1.5. The resulting float would be of the form [0 0111111 [the byte] 0..0], such that the byte returned
	//is the only unknown value
	public static byte getByteFromSmallFloat(float f){
		MathHelper.clamp_float(f, 0.5f, 1.5f);
		return (byte)((Float.floatToIntBits(f) >> 16) & 0xff);
	}

	public static float getSmallFloatFromByte(byte b)
	{
		return ByteBuffer.wrap(new byte[]{(byte)63, b,(byte)(0),(byte)0}).getFloat();
	}

	/**
	 * This version of the method assumes that the environmental decay modifier
	 * has already been calculated.
	 */
	public static void handleItemTicking(IInventory iinv, World world, int x, int y, int z, float environmentalDecayFactor, float baseDecayMod)
	{
		for (int i = 0; !world.isRemote && i < iinv.getSizeInventory(); i++)
		{
			ItemStack is = iinv.getStackInSlot(i);
			if (is != null && iinv.getStackInSlot(i).stackSize <= 0)
				iinv.setInventorySlotContents(i, null);

			if (is != null)
			{
				if (is.getItem() instanceof ItemTerra && ((ItemTerra) is.getItem()).onUpdate(is, world, x, y, z))
					continue;
				else if (is.getItem() instanceof ItemTerraBlock && ((ItemTerraBlock) is.getItem()).onUpdate(is, world, x, y, z))
					continue;
				is = tickDecay(is, world, x, y, z, environmentalDecayFactor, baseDecayMod);
				if(is != null)
					TFC_ItemHeat.handleItemHeat(is);
				iinv.setInventorySlotContents(i, is);
			}
		}
	}

	/**
	 * This version of the method assumes that the environmental decay modifier
	 * has already been calculated.
	 */
	public static void handleItemTicking(ItemStack[] iinv, World world, int x, int y, int z, float environmentalDecayFactor)
	{
		for (int i = 0; !world.isRemote && i < iinv.length; i++)
		{
			ItemStack is = iinv[i];
			if (is != null && iinv[i].stackSize <= 0)
				iinv[i] = null;

			if (is != null)
			{
				if (is.getItem() instanceof ItemTerra && ((ItemTerra) is.getItem()).onUpdate(is, world, x, y, z))
					continue;
				else if (is.getItem() instanceof ItemTerraBlock && ((ItemTerraBlock) is.getItem()).onUpdate(is, world, x, y, z))
					continue;
				is = tickDecay(is, world, x, y, z, environmentalDecayFactor, 1);
				if(is != null)
					TFC_ItemHeat.handleItemHeat(is);
				iinv[i] = is;
			}

		}
	}

	/**
	 * @param is
	 * @param baseDecayMod
	 * @param nbt
	 */
	public static ItemStack tickDecay(ItemStack is, World world, int x, int y, int z, float environmentalDecayFactor, float baseDecayMod)
	{
		NBTTagCompound nbt = is.getTagCompound();
		if (nbt == null || !nbt.hasKey(Food.WEIGHT_TAG) || !nbt.hasKey(Food.DECAY_TAG))
			return is;

		int decayTimer = Food.getDecayTimer(is);
		// if the tick timer is up then we cause decay.
		if (decayTimer < TFC_Time.getTotalHours())
		{
			int timeDiff = (int) (TFC_Time.getTotalHours() - decayTimer);
			float protMult = 1;

			if(TFCOptions.useDecayProtection)
			{
				if(timeDiff > TFCOptions.decayProtectionDays * 24)
				{
					decayTimer = (int) TFC_Time.getTotalHours() - 24;
				}
				else if(timeDiff > 24)
				{
					protMult = 1-(timeDiff/(TFCOptions.decayProtectionDays * 24));
				}
			}

			float decay = Food.getDecay(is);
			float thisDecayRate = 1.0f;
			// Get the base food decay rate
			if (is.getItem() instanceof IFood)
				thisDecayRate = ((IFood) is.getItem()).getDecayRate(is);
			// check if the food has a specially applied decay rate in its nbt (meals, sandwiches, salads)
			else
				thisDecayRate = Food.getDecayRate(is);

			/*
			 * Here we calculate the decayRate based on the environment. We do
			 * this before everything else so that its only done once per
			 * inventory
			 */
			//int day = TFC_Time.getDayOfYearFromDays(TFC_Time.getDayFromTotalHours(nbt.getInteger(Food.DECAY_TIMER_TAG)));
			//float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(world,day,nbt.getInteger(Food.DECAY_TIMER_TAG), x, y, z);
			float temp = getCachedTemp(world, x, y, z, decayTimer);
			float environmentalDecay = getEnvironmentalDecay(temp) * environmentalDecayFactor;

			if (decay < 0)
			{
				float d = 1 * (thisDecayRate * baseDecayMod * environmentalDecay);
				if (decay + d < 0)
					decay += d;
				else
					decay = 0;
			}
			else if (decay == 0)
			{
				decay = (Food.getWeight(is) * (world.rand.nextFloat() * 0.005f)) * TFCOptions.decayMultiplier;
			}
			else
			{
				double fdr = TFCOptions.foodDecayRate - 1;
				fdr *= thisDecayRate * baseDecayMod * environmentalDecay * protMult * TFCOptions.decayMultiplier;
				decay *= 1 + fdr;
			}
			Food.setDecayTimer(is, decayTimer + 1);
			Food.setDecay(is, decay);
		}

		if (Food.getDecay(is) / Food.getWeight(is) > 0.9f)
		{
			if(is.getItem() instanceof IFood)
				is = ((IFood)is.getItem()).onDecayed(is, world, x, y, z);
			else
				is.stackSize = 0;
		}

		return is;
	}

	public static float getCachedTemp(World world, int x, int y, int z, int th)
	{
		float cacheTemp = TFC_Climate.getCacheManager(world).getTemp(x, z, th);
		if(cacheTemp != Float.MIN_VALUE)
		{
			return cacheTemp;
		}
		float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(world,TFC_Time.getDayFromTotalHours(th), TFC_Time.getHourOfDayFromTotalHours(th), x, y, z);
		addCachedTemp(world, x, z, th, temp);
		return temp;
	}

	public static void addCachedTemp(World world, int x, int z, int th, float temp)
	{
		TFC_Climate.getCacheManager(world).addTemp(x, z, th, temp);
	}

	public static void animalDropMeat(Entity e, Item i, float foodWeight)
	{
		Random r;
		ItemStack is = ItemFoodTFC.createTag(new ItemStack(i, 1), foodWeight);
		r = new Random(e.getUniqueID().getLeastSignificantBits() + e.getUniqueID().getMostSignificantBits());
		Food.adjustFlavor(is, r);
		e.capturedDrops.add(new EntityItem(e.worldObj, e.posX, e.posY, e.posZ, is));
	}

	public static Vec3 getEntityPos(Entity e)
	{
		return Vec3.createVectorHelper(e.posX, e.posY, e.posZ);
	}

	public static void giveItemToPlayer(ItemStack is, EntityPlayer player)
	{
		if(player.worldObj.isRemote)
			return;
		EntityItem ei = player.entityDropItem(is, 1);
		ei.delayBeforeCanPickup = 0;
	}

	public static boolean isFence(Block b)
	{
		return b == fence || b == fence2;
	}

	public static boolean isVertSupport(Block b)
	{
		return b == woodSupportV || b == woodSupportV2;
	}

	public static boolean isHorizSupport(Block b)
	{
		return b == woodSupportH || b == woodSupportH2;
	}

	public static boolean isOceanicBiome(int id)
	{
		return id == TFCBiome.OCEAN.biomeID || id == TFCBiome.DEEP_OCEAN.biomeID;
	}

	public static boolean isMountainBiome(int id)
	{
		return id == TFCBiome.MOUNTAINS.biomeID || id == TFCBiome.MOUNTAINS_EDGE.biomeID;
	}

	public static boolean isBeachBiome(int id)
	{
		return id == TFCBiome.BEACH.biomeID || id == TFCBiome.GRAVEL_BEACH.biomeID;
	}

	public static boolean isValidCharcoalPitCover(Block block)
	{
		if(Blocks.fire.getFlammability(block) > 0 && block != logPile) return false;

		return block == logPile
				|| isCobbleStone(block)
				|| isBrickStone(block)
				|| isSmoothStone(block)
				|| isGround(block)
				|| block == Blocks.glass
				|| block == Blocks.stained_glass
				|| block == metalTrapDoor
				|| block == Blocks.iron_door
				|| block.isOpaqueCube();
	}

	public static void writeInventoryToNBT(NBTTagCompound nbt, ItemStack[] storage)
	{
		writeInventoryToNBT(nbt, storage, "Items");
	}

	public static void writeInventoryToNBT(NBTTagCompound nbt, ItemStack[] storage, String name)
	{
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < storage.length; i++)
		{
			if(storage[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				storage[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag(name, nbttaglist);
	}

	public static void readInventoryFromNBT(NBTTagCompound nbt, ItemStack[] storage)
	{
		readInventoryFromNBT(nbt, storage, "Items");
	}

	public static void readInventoryFromNBT(NBTTagCompound nbt, ItemStack[] storage, String name)
	{
		NBTTagList nbttaglist = nbt.getTagList(name, 10);
		for(int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < storage.length)
				storage[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	public static ItemStack getItemInInventory(Item item, IInventory iinv)
	{
		for(int i = 0; i < iinv.getSizeInventory(); i++)
		{
			iinv.getStackInSlot(i);
			if(iinv.getStackInSlot(i) != null && iinv.getStackInSlot(i).getItem() == item)
			{
				return iinv.getStackInSlot(i);
			}
		}
		return null;
	}

	public static void destroyBlock(World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z) != Blocks.air)
		{
			world.getBlock(x, y, z).dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	public static boolean areItemsEqual(ItemStack is1, ItemStack is2)
	{
		Item i1 = null; int d1 = 0;
		Item i2 = null; int d2 = 0;
		if(is1 != null)
		{
			i1 = is1.getItem(); d1 = is1.getItemDamage();
		}
		if(is2 != null)
		{
			i2 = is2.getItem(); d2 = is2.getItemDamage();
		}
		return i1 == i2 && d1 == d2;
	}

	public static boolean setBlockWithDrops(World world, int x, int y, int z, Block b, int meta)
	{
		Block block = world.getBlock(x, y, z);

		if (block.getMaterial() != Material.air)
		{
			int l = world.getBlockMetadata(x, y, z);
			world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (l << 12));
			block.dropBlockAsItem(world, x, y, z, l, 0);
		}
		return world.setBlock(x, y, z, b, meta, 3);
	}

	/**
	 * This is a wrapper method for the vanilla world method with no MCP mapping
	 */
	public static boolean setBlockToAirWithDrops(World world, int x, int y, int z)
	{
		return world.func_147480_a(x, y, z, true);
	}

	public static boolean isWaterBiome(BiomeGenBase b)
	{
		return TFC_Core.isBeachBiome(b.biomeID) || TFC_Core.isOceanicBiome(b.biomeID) || b == TFCBiome.LAKE || b == TFCBiome.RIVER;
	}

	public static String translate(String s)
	{
		return StatCollector.translateToLocal(s);
	}

	public static void sendInfoMessage(EntityPlayer player, IChatComponent text)
	{
		text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
		player.addChatComponentMessage(text);
	}

	public static long getSuperSeed(World w)
	{
		return w.getSeed()+w.getWorldInfo().getNBTTagCompound().getLong("superseed");
	}
	
	public static boolean isExposedToRain(World world, int x, int y, int z)
	{
		int highestY = world.getPrecipitationHeight(x, z) - 1;
		boolean isExposed = true;
		if (world.canBlockSeeTheSky(x, y + 1, z)) // Either no blocks, or transparent blocks above.
		{
			// Glass blocks, or blocks with a solid top or bottom block the rain.
			if (world.getBlock(x, highestY, z) instanceof BlockGlass
					|| world.getBlock(x, highestY, z) instanceof BlockStainedGlass
					|| world.isSideSolid(x, highestY, z, ForgeDirection.UP) 
					|| world.isSideSolid(x, highestY, z, ForgeDirection.DOWN))
				isExposed = false;
		}
		else // Can't see the sky
			isExposed = false;

		return world.isRaining() && isExposed;
	}
}
