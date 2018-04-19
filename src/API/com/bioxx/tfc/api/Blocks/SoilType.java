package com.bioxx.tfc.api.Blocks;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import net.minecraft.block.Block;

public class SoilType { 

	private final Class blockClass;
	private Block[] blocks;
	private final String name;

	private static final List<SoilType> INSTANCES = new ArrayList<>();

	public SoilType(Class<? extends Block> blockClass, String name) {
		this.blockClass = blockClass;
		this.name = name;
		INSTANCES.add(this);
	}

	public String getName() {
		return name;
	}

	public void registerBlock() {
		registerBlock((b, i) -> {});
	}

	public SoilType registerBlock(BiConsumer<Block, Integer> propertySetter) {
		Objects.requireNonNull(propertySetter);
		try {
			Constructor<Block> constructor = blockClass.getConstructor(int.class);
			int count = (StoneVariant.getVariants().length + 15) / 16;
			blocks = new Block[count];
			for (int i = 0; i < count; i++) {
				blocks[i] = constructor.newInstance(i * 16);
				propertySetter.accept(blocks[i], Integer.valueOf(i * 16));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public Block getBlock(int meta) {
		int index = meta / 16;
		return blocks == null || blocks.length == 0 ? null : meta < 0 || index >= blocks.length ? blocks[0] : blocks[index];
	}

	public Block getBlockAt(int index) {
		return blocks == null || blocks.length == 0 ? null : index < 0 || index >= blocks.length ? blocks[0] : blocks[index];
	}

	public int getIndex(Block block) {
		if (blocks != null) {
			for (int i = 0; i < blocks.length; i++) if (block == blocks[i]) return i;
		}
		return -1;
	}

	public static int getIndexIfAnyMatch(Block block, SoilType... types) {
		int index;
		for (SoilType st : types) if ((index = st.getIndex(block)) >= 0) return index;
		return -1;
	}

	public List<Block> getAllBlocks() {
		return Arrays.asList(blocks);
	}

	public boolean isInstance(Block block) {
		if (blocks == null) return false;
		for (Block b : blocks) if (b == block) return true;
		return false;
	}

	public static List<SoilType> getAllSoilTypes() {
		return new ArrayList<>(INSTANCES);
	}

}
