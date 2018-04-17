package com.bioxx.tfc.api.Constant;

import java.util.Arrays;

import com.bioxx.tfc.Blocks.Terrain.StoneType;
import com.bioxx.tfc.Blocks.Terrain.StoneVariant;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCOptions;
import com.google.common.collect.ObjectArrays;

import net.minecraftforge.common.util.ForgeDirection;
import static net.minecraftforge.common.util.ForgeDirection.*;

@SuppressWarnings("PMD")
public class Global
{
	public static final ForgeDirection[] SIDES = {NORTH, SOUTH, WEST, EAST};

	/* FruitTree Meta Names, also used for fruit items and FloraManager */
	public static final String[] FRUIT_META_NAMES = new String[] {
		"Red Apple", "Banana", "Orange", "Green Apple", "Lemon", "Olive", "Cherry", "Peach", "Plum"
	};

	/* Flower Meta Names
	 * The first 10 flowers are from vanilla */
	public static final String[] FLOWER_META_NAMES = new String[] {
		"flower_rose", "flower_blue_orchid", "flower_allium", "flower_houstonia",
		"flower_tulip_red", "flower_tulip_orange", "flower_tulip_white", "flower_tulip_pink", "flower_oxeye_daisy",
		"flower_dandelion","flower_nasturtium", "flower_meads_milkweed", "flower_tropical_milkweed", "flower_butterfly_milkweed", "flower_calendula"
	};

	/* Fungi Meta Names
	 * The first 2 are vanilla mushrooms */
	public static final String[] FUNGI_META_NAMES = new String[] {
		"mushroom_brown", "mushroom_red"
	};

	/* Powder */
	public static final String[] POWDER = {
		"Flux", "Kaolinite Powder", "Graphite Powder", "Sulfur Powder", "Saltpeter Powder",
		"Hematite Powder", "Lapis Lazuli Powder", "Limonite Powder", "Malachite Powder", "Salt"
	};
	
	/* Dyes, used for carpets and small vessels */
	/* Colors MUST be in the same order as in EntitySheep.fleeceColorTable! */
	public static final String[] DYE_NAMES = { 
		"dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue", "dyeYellow",
		"dyeLime", "dyePink", "dyeGray", "dyeLightGray", "dyeCyan", 
		"dyePurple", "dyeBlue", "dyeBrown", "dyeGreen", "dyeRed", "dyeBlack" };

	public static final String STONETYPE_IGIN = "igneous intrusive";
	public static final String STONETYPE_SED = "sedimentary";
	public static final String STONETYPE_IGEX = "igneous extrusive";
	public static final String STONETYPE_MM = "metamorphic";

	public static final StoneVariant Granite		= new StoneVariant("Granite",		STONETYPE_IGIN,	0,  0);
	public static final StoneVariant Diorite		= new StoneVariant("Diorite",		STONETYPE_IGIN,	1,  1);
	public static final StoneVariant Gabbro			= new StoneVariant("Gabbro",		STONETYPE_IGIN,	2,  2);
	public static final StoneVariant Shale			= new StoneVariant("Shale",			STONETYPE_SED,	0,  3);
	public static final StoneVariant Claystone		= new StoneVariant("Claystone",		STONETYPE_SED,	1,  4);
	public static final StoneVariant Rock_Salt		= new StoneVariant("Rock Salt",		STONETYPE_SED,	2,  5);
	public static final StoneVariant Limestone		= new StoneVariant("Limestone",		STONETYPE_SED,	3,  6);
	public static final StoneVariant Conglomerate	= new StoneVariant("Conglomerate",	STONETYPE_SED,	4,  7);
	public static final StoneVariant Dolomite		= new StoneVariant("Dolomite",		STONETYPE_SED,	5,  8);
	public static final StoneVariant Chert			= new StoneVariant("Chert",			STONETYPE_SED,	6,  9);
	public static final StoneVariant Chalk			= new StoneVariant("Chalk",			STONETYPE_SED,	7, 10);
	public static final StoneVariant Rhyolite		= new StoneVariant("Rhyolite",		STONETYPE_IGEX,	0, 11);
	public static final StoneVariant Basalt			= new StoneVariant("Basalt",		STONETYPE_IGEX,	1, 12);
	public static final StoneVariant Andesite		= new StoneVariant("Andesite",		STONETYPE_IGEX,	2, 13);
	public static final StoneVariant Dacite			= new StoneVariant("Dacite",		STONETYPE_IGEX,	3, 14);
	public static final StoneVariant Quartzite		= new StoneVariant("Quartzite",		STONETYPE_MM,	0, 15);
	public static final StoneVariant Slate			= new StoneVariant("Slate",			STONETYPE_MM,	1, 16);
	public static final StoneVariant Phyllite		= new StoneVariant("Phyllite",		STONETYPE_MM,	2, 17);
	public static final StoneVariant Schist			= new StoneVariant("Schist",		STONETYPE_MM,	3, 18);
	public static final StoneVariant Gneiss			= new StoneVariant("Gneiss",		STONETYPE_MM,	4, 19);
	public static final StoneVariant Marble			= new StoneVariant("Marble",		STONETYPE_MM,	5, 20);

	static {
		Limestone.setFlux(true);
		Dolomite.setFlux(true);
		Chalk.setFlux(true);
		Marble.setFlux(true);
	}

	/* Stone Types */
	@Deprecated
	public static final String[] STONE_IGIN = StoneType.getType(STONETYPE_IGIN).getVariantNames().toArray(new String[0]);
	@Deprecated
	public static final String[] STONE_SED  = StoneType.getType(STONETYPE_SED).getVariantNames().toArray(new String[0]);
	@Deprecated
	public static final String[] STONE_IGEX = StoneType.getType(STONETYPE_IGEX).getVariantNames().toArray(new String[0]);
	@Deprecated
	public static final String[] STONE_MM   = StoneType.getType(STONETYPE_MM).getVariantNames().toArray(new String[0]);

	// Used for loose rocks and other places where the stone list is combined
	@Deprecated
	public static final int STONE_IGIN_START = 0;
	@Deprecated
	public static final int STONE_SED_START = STONE_IGIN_START + STONE_IGIN.length;
	@Deprecated
	public static final int STONE_IGEX_START = STONE_SED_START + STONE_SED.length;
	@Deprecated
	public static final int STONE_MM_START = STONE_IGEX_START + STONE_IGEX.length;
	@Deprecated
	public static final String[] STONE_ALL  = StoneVariant.getAllVariantNames().toArray(new String[0]);

	// Stones that can be turned into flux
	@Deprecated
	public static final int[] STONE_FLUXINDEX = StoneVariant.getAllVariants().stream().filter(StoneVariant::isFlux).mapToInt(StoneVariant::getGeneralIndex).toArray();

	/* Ore Types */
	public static final String[] ORE_METAL = {
		"Native Copper", "Native Gold", "Native Platinum", "Hematite",
		"Native Silver", "Cassiterite", "Galena", "Bismuthinite",
		"Garnierite", "Malachite", "Magnetite", "Limonite",
		"Sphalerite", "Tetrahedrite", "Bituminous Coal", "Lignite"
	};
	public static final String[] ORE_MINERAL = {
		"Kaolinite", "Gypsum", "Satinspar", "Selenite",
		"Graphite", "Kimberlite", "Petrified Wood", "Sulfur",
		"Jet", "Microcline", "Pitchblende", "Cinnabar",
		"Cryolite", "Saltpeter", "Serpentine", "Sylvite"
	};
	public static final String[] ORE_MINERAL2 = {"Borax", "Olivine", "Lapis Lazuli"};

	public static final String[] WOOD_ALL = {
		"Oak","Aspen","Birch","Chestnut",
		"Douglas Fir","Hickory","Maple","Ash",
		"Pine","Sequoia","Spruce","Sycamore",
		"White Cedar","White Elm","Willow","Kapok","Acacia","temp1","temp2"
	};

	public static final String SKILL_GENERAL_SMITHING = "skill.gensmith";
	public static final String SKILL_TOOLSMITH = "skill.toolsmith";
	public static final String SKILL_WEAPONSMITH = "skill.weaponsmith";
	public static final String SKILL_ARMORSMITH = "skill.armorsmith";
	public static final String SKILL_AGRICULTURE = "skill.agriculture";
	public static final String SKILL_COOKING = "skill.cooking";
	public static final String SKILL_PROSPECTING = "skill.prospecting";
	public static final String SKILL_BUTCHERING = "skill.butchering";

	public static Metal BISMUTH;
	public static Metal BISMUTHBRONZE;
	public static Metal BLACKBRONZE;
	public static Metal BLACKSTEEL;
	public static Metal BLUESTEEL;
	public static Metal BRASS;
	public static Metal BRONZE;
	public static Metal COPPER;
	public static Metal GOLD;
	public static Metal WROUGHTIRON;
	public static Metal LEAD;
	public static Metal NICKEL;
	public static Metal PIGIRON;
	public static Metal PLATINUM;
	public static Metal REDSTEEL;
	public static Metal ROSEGOLD;
	public static Metal SILVER;
	public static Metal STEEL;
	public static Metal STERLINGSILVER;
	public static Metal TIN;
	public static Metal ZINC;
	public static Metal WEAKSTEEL;
	public static Metal HCBLACKSTEEL;
	public static Metal WEAKREDSTEEL;
	public static Metal HCREDSTEEL;
	public static Metal WEAKBLUESTEEL;
	public static Metal HCBLUESTEEL;
	public static Metal UNKNOWN;

	/**
	 * Switch to TFCOptions.foodDecayRate
	 */
	@Deprecated
	public static double foodDecayRate = TFCOptions.foodDecayRate;

	public static final float FOOD_MAX_WEIGHT = 160;
	public static final float FOOD_MIN_DROP_WEIGHT = 0.1f;

	public static final int SEALEVEL = 144;
	
	public static final int HOT_LIQUID_TEMP = 385; // Water's boiling point is 373.2 K
}
