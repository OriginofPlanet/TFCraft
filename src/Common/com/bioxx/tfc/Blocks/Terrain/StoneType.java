package com.bioxx.tfc.Blocks.Terrain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

	private BlockStone stone;
	private BlockCobble cobble;
	private BlockSmooth smooth;
	private BlockBrick brick;
	private BlockCustomWall wallStone, wallCobble, wallSmooth, wallBrick;
	
	private int cobbleTickRate = 10;
	private int stoneGemChance = 0;

	private static final Map<String, StoneType> types = Maps.newHashMap();

	protected StoneType(String name) {
		this.name = name;
		types.put(name, this);
		isAvailable = true;
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
		return Optional.ofNullable(index < 0 || index > variants.length ? null : variants[index]).orElse(StoneVariant.UNKOWN_VARIANT);
	}

	public StoneType setStoneGemChance(int stoneGemChance) {
		this.stoneGemChance = stoneGemChance;
		return this;
	}

	public StoneType setCobbleTickRate(int cobbleTickRate) {
		this.cobbleTickRate = cobbleTickRate;
		return this;
	}

	public void registerBlocks() {
		cobble = new BlockCobble(this, cobbleTickRate);
		stone = new BlockStone(this, stoneGemChance);
		smooth = new BlockSmooth(this);
		brick = new BlockBrick(this);
		wallCobble = new BlockCustomWall(cobble, this);
		wallStone = new BlockCustomWall(stone, this);
		wallSmooth = new BlockCustomWall(smooth, this);
		wallBrick = new BlockCustomWall(brick, this);
	}

	public Block getStone() {
		return stone;
	}

	public Block getCobble() {
		return cobble;
	}

	public Block getSmooth() {
		return smooth;
	}

	public Block getBrick() {
		return brick;
	}

	public BlockCustomWall getWallCobble() {
		return wallCobble;
	}

	public BlockCustomWall getWallStone() {
		return wallStone;
	}

	public BlockCustomWall getWallSmooth() {
		return wallSmooth;
	}

	public BlockCustomWall getWallBrick() {
		return wallBrick;
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
