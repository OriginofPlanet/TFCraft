package com.bioxx.tfc.api.Blocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.bioxx.tfc.Blocks.Terrain.BlockBrick;
import com.bioxx.tfc.Blocks.Terrain.BlockCobble;
import com.bioxx.tfc.Blocks.Terrain.BlockSmooth;
import com.bioxx.tfc.Blocks.Terrain.BlockStone;
import com.bioxx.tfc.Blocks.Vanilla.BlockCustomWall;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;

public class StoneType {

	protected static final StoneType UNKOWN_TYPE = new StoneType();

	private final String name;
	private final boolean isAvailable;
	private final StoneVariant[] variants = new StoneVariant[8];
	{
		Arrays.fill(variants, StoneVariant.UNKOWN_VARIANT);
	}

	private boolean isAnvil = false;
	private String abbr;

	//stone, cobble, smooth, brick, wallStone, wallCobble, wallSmooth, wallBrick
	private Block[] blocks = new Block[8];

	private static final Map<String, StoneType> types = Maps.newHashMap();

	protected StoneType(String name) {
		this.name = name;
		types.put(name, this);
		isAvailable = true;
		abbr = Arrays.asList(name.split(" ")).stream().map(s -> s.length() < 2 ? upperCase(s) : upperCase(s).substring(0, 2)).collect(Collectors.joining());
	}

	private static final int upperdiff = 'A' - 'a';
	private static String upperCase(String string) {
		char[] c = string.toCharArray();
		if (c[0] >= 'a' && c[0] <= 'z') {
			c[0] = (char) (c[0] + upperdiff);
		}
		return new String(c);
	}

	private StoneType() {
		this.name = "Unkown Type";
		isAvailable = false;
	}

	protected StoneType registerVariant(int index, StoneVariant variant) {
		if (variants[index].isAvailable()) throw new IllegalArgumentException("Index " + index + " is already registered");
		variants[index] = variant;
		return this;
	}

	public String getAbbr() {
		return abbr;
	}

	public StoneType setAbbr(String abbr) {
		this.abbr = abbr;
		return this;
	}

	public StoneType setAnvil(boolean isAnvil) {
		this.isAnvil = isAnvil;
		return this;
	}

	public boolean isAnvil() {
		return isAnvil;
	}

	public String getName() {
		return name;
	}

	public List<StoneVariant> getVariants() {
		return Arrays.stream(variants).filter(StoneVariant::isAvailable).collect(Collectors.toList());
	}

	public List<String> getVariantNames() {
		return Arrays.stream(variants).filter(StoneVariant::isAvailable).map(StoneVariant::getName).collect(Collectors.toList());
	}

	public StoneVariant get(int index) {
		return index < 0 || index >= variants.length ? StoneVariant.UNKOWN_VARIANT : variants[index];
	}

	public static interface IBlockConstructor {
		public Block newCobble(StoneType stoneType);
		public Block newStone(StoneType stoneType);
		public Block newSmooth(StoneType stoneType);
		public Block newBrick(StoneType stoneType);
		public Block newWall(Block block, StoneType stoneType);
	}

	public StoneType registerBlocks(IBlockConstructor constructor, Consumer<StoneType> propertySetter) {
		if (isAvailable && constructor != null) {
			blocks[1] = constructor.newCobble(this);
			blocks[0] = constructor.newStone(this);
			blocks[2] = constructor.newSmooth(this);
			blocks[3] = constructor.newBrick(this);
			for (int i = 0; i < 4; i++) blocks[i + 4] = constructor.newWall(blocks[i], this);
			propertySetter.accept(this);
		}
		return this;
	}

	public Block getStone() {
		return blocks[0];
	}

	public Block getCobble() {
		return blocks[1];
	}

	public Block getSmooth() {
		return blocks[2];
	}

	public Block getBrick() {
		return blocks[3];
	}

	public Block getWallStone() {
		return blocks[4];
	}

	public Block getWallCobble() {
		return blocks[5];
	}

	public Block getWallSmooth() {
		return blocks[6];
	}

	public Block getWallBrick() {
		return blocks[7];
	}

	public List<Block> getAllBlocks() {
		return Arrays.asList(blocks);
	}

	public static Collection<StoneType> getAllTypes() {
		return types.values();
	}

	public static Collection<String> getAllTypeNames() {
		return types.keySet();
	}

	public static StoneType getType(String name) {
		return types.getOrDefault(name, UNKOWN_TYPE);
	}

	protected static StoneType getOrCreate(String name) {
		return types.computeIfAbsent(name, StoneType::new);
	}

}
