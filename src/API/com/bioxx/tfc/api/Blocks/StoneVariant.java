package com.bioxx.tfc.api.Blocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StoneVariant {

	protected static final StoneVariant UNKOWN_VARIANT = new StoneVariant();

	private final String name;
	private final int localIndex, generalIndex;
	private final StoneType type;
	private final boolean isAvailable;
	
	private boolean isFlux = false;

	private static StoneVariant[] variants = new StoneVariant[10];
	private static int maxID = -1;
	static {
		Arrays.fill(variants, UNKOWN_VARIANT);
	}

	public StoneVariant(String name, String type, int localIndex, int generalIndex) {
		if (localIndex >= 8) throw new IllegalArgumentException("Local index should be at most 7");
		this.name = name;
		this.localIndex = localIndex;
		this.generalIndex = generalIndex;
		register(generalIndex, this);
		this.type = StoneType.getOrCreate(type).registerVariant(localIndex, this);
		isAvailable = true;
	}

	private StoneVariant() {
		this.name = "Unkown Variant";
		this.type = StoneType.UNKOWN_TYPE;
		this.localIndex = 0;
		this.generalIndex = 0;
		isAvailable = false;
	}

	private static void register(int index, StoneVariant element) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException();
		if (variants.length <= index) {
			StoneVariant[] newList = new StoneVariant[Math.max((int) (variants.length * 1.5F), index + 1)];
			Arrays.fill(newList, UNKOWN_VARIANT);
			System.arraycopy(variants, 0, newList, 0, variants.length);
			variants = newList;
		}
		if (variants[index].isAvailable) throw new IllegalArgumentException("Index " + index + " is already registered");
		variants[index] = element;
		if (index > maxID) maxID = index;
	}

	public void setFlux(boolean isFlux) {
		this.isFlux = isFlux;
	}

	public String getName() {
		return name;
	}

	public StoneType getType() {
		return type;
	}

	public int getLocalIndex() {
		return localIndex;
	}

	public int getGeneralIndex() {
		return generalIndex;
	}

	public boolean isFlux() {
		return isFlux;
	}

	public static StoneVariant get(String name) {
		for (StoneVariant sv : variants) if (sv.name.equals(name)) return sv;
		return UNKOWN_VARIANT;
	}

	public static StoneVariant get(int index) {
		return index < 0 || index >= variants.length ? UNKOWN_VARIANT : variants[index];
	}

	public static List<StoneVariant> getAllVariants() {
		return Arrays.stream(variants).filter(StoneVariant::isAvailable).collect(Collectors.toList());
	}

	public static List<String> getAllVariantNames() {
		return Arrays.stream(variants).filter(StoneVariant::isAvailable).map(StoneVariant::getName).collect(Collectors.toList());
	}

	public static StoneVariant[] getVariants() {
		return Arrays.copyOf(variants, maxID + 1);
	}

	public boolean isAvailable() {
		return isAvailable;
	}

}
