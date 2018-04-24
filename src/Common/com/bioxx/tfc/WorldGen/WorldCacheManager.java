package com.bioxx.tfc.WorldGen;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.IntCache;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.WorldGen.Data.DataCache;
import com.bioxx.tfc.WorldGen.GenLayers.GenLayerTFC;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.Drainage.GenDrainageLayer;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.EVT.GenEVTLayer;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.PH.GenPHLayer;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.Rain.GenRainLayerTFC;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.Rock.GenRockLayer;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.Stability.GenStabilityLayer;
import com.bioxx.tfc.WorldGen.GenLayers.DataLayers.Tree.GenTreeLayer;

public class WorldCacheManager 
{	
	//private World localWorld;
	//Rocks
	protected GenLayerTFC[] rocksIndexLayer;
	protected DataCache[] rockCache;

	//Trees
	protected GenLayerTFC[] treesIndexLayer;
	protected DataCache[] treeCache;

	//Humidity
	protected GenLayerTFC evtIndexLayer;
	protected DataCache evtCache;

	//Rainfall
	protected GenLayerTFC rainfallIndexLayer;
	protected DataCache rainfallCache;

	//Stability
	protected GenLayerTFC stabilityIndexLayer;
	protected DataCache stabilityCache;

	//Stability
	protected GenLayerTFC phIndexLayer;
	protected DataCache phCache;

	//Stability
	protected GenLayerTFC drainageIndexLayer;
	protected DataCache drainageCache;

	protected Map<String, Float> worldTempCache;

	public long seed;

	public static final DataLayer[][] ROCK_LAYERS = {
			{	DataLayer.SHALE, DataLayer.CLAYSTONE, DataLayer.ROCKSALT, DataLayer.LIMESTONE,
				DataLayer.CONGLOMERATE, DataLayer.DOLOMITE, DataLayer.CHERT, DataLayer.CHALK,
				DataLayer.RHYOLITE, DataLayer.BASALT, DataLayer.ANDESITE, DataLayer.DACITE,
				DataLayer.QUARTZITE, DataLayer.SLATE, DataLayer.PHYLLITE, DataLayer.SCHIST,
				DataLayer.GNEISS, DataLayer.MARBLE, DataLayer.GRANITE, DataLayer.DIORITE, DataLayer.GABBRO},
			{	DataLayer.RHYOLITE, DataLayer.BASALT, DataLayer.ANDESITE, DataLayer.DACITE,
				DataLayer.QUARTZITE, DataLayer.SLATE, DataLayer.PHYLLITE, DataLayer.SCHIST,
				DataLayer.GNEISS, DataLayer.MARBLE, DataLayer.GRANITE, DataLayer.DIORITE,
				DataLayer.GABBRO},
			{	DataLayer.RHYOLITE, DataLayer.BASALT, DataLayer.ANDESITE,
				DataLayer.DACITE, DataLayer.GRANITE, DataLayer.DIORITE, DataLayer.GABBRO},
	};

	@Deprecated
	public static final DataLayer[] ROCK_LAYER_1 = ROCK_LAYERS[0];
	@Deprecated
	public static final DataLayer[] ROCK_LAYER_2 = ROCK_LAYERS[1];
	@Deprecated
	public static final DataLayer[] ROCK_LAYER_3 = ROCK_LAYERS[2];

	public static final int ROCK_LAYER_COUNT = ROCK_LAYERS.length;
	/**
	 * Must have ROCK_LAYER_COUNT intervals.
	 */
	public static final int[] ROCK_LAYER_SPLITERATOR = {Integer.MIN_VALUE, 55, 110, Integer.MAX_VALUE};

	public static final DataLayer[] TREE_ARRAY = new DataLayer[] {DataLayer.ASH, DataLayer.ASPEN, DataLayer.BIRCH, DataLayer.CHESTNUT, DataLayer.DOUGLASFIR, 
		DataLayer.HICKORY, DataLayer.MAPLE, DataLayer.OAK, DataLayer.PINE, DataLayer.REDWOOD, DataLayer.PINE, DataLayer.SPRUCE, DataLayer.SYCAMORE, 
		DataLayer.WHITECEDAR, DataLayer.WHITEELM, DataLayer.WILLOW, DataLayer.NO_TREE};

	private WorldCacheManager()
	{
		rockCache = new DataCache[ROCK_LAYER_COUNT];
		treeCache = new DataCache[3];
		evtCache = new DataCache(this, 0);
		rainfallCache = new DataCache(this, 0);
		for (int i = 0; i < rockCache.length; i++) rockCache[i] = new DataCache(this, i);
		for (int i = 0; i < treeCache.length; i++) treeCache[i] = new DataCache(this, i);
		stabilityCache = new DataCache(this, 0);
		phCache = new DataCache(this, 0);
		drainageCache = new DataCache(this, 0);
		worldTempCache = new LinkedHashMap<String, Float>();
	}

	public WorldCacheManager(World world)
	{
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		//localWorld = world;
	}

	private WorldCacheManager(long genSeed, WorldType worldtype)
	{
		this();
		seed = genSeed;

		//Setup Rocks
		rocksIndexLayer = new GenLayerTFC[ROCK_LAYER_COUNT];
		for (int i = 0; i < rocksIndexLayer.length; i++) rocksIndexLayer[i] = GenRockLayer.initialize(++genSeed, ROCK_LAYERS[i]);

		//Setup Trees
		treesIndexLayer = new GenLayerTFC[3];
		for (int i = 0; i < treesIndexLayer.length; i++) treesIndexLayer[i] = GenRockLayer.initialize(++genSeed, TREE_ARRAY);

		//Setup Evapotranspiration
		evtIndexLayer = GenEVTLayer.initialize(++genSeed, worldtype);

		//Setup Rainfall
		rainfallIndexLayer = GenRainLayerTFC.initialize(++genSeed, worldtype);

		//Setup Stability
		stabilityIndexLayer = GenStabilityLayer.initialize(++genSeed, worldtype);

		//Setup Soil PH
		phIndexLayer = GenPHLayer.initialize(++genSeed, worldtype);

		//Setup Soil Drainage
		drainageIndexLayer = GenDrainageLayer.initialize(++genSeed, worldtype);

		worldTempCache = new LinkedHashMap<String, Float>();
	}

	public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5)
	{
		IntCache.resetIntCache();

		if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
			par1ArrayOfFloat = new float[par4 * par5];

		int[] var6 = this.rainfallIndexLayer.getInts(par2, par3, par4, par5);

		for (int var7 = 0; var7 < par4 * par5; ++var7)
		{
			float var8 = var6[var7];
			var8 = 1-((8000-var8) / 8000);
			if (var8 > 1.0F)
				var8 = 1.0F;
			par1ArrayOfFloat[var7] = var8;
		}

		return par1ArrayOfFloat;
	}

	public void cleanupCache()
	{
		for (DataCache rc : rockCache) rc.cleanupCache();
		for (DataCache tc : treeCache) tc.cleanupCache();
		this.evtCache.cleanupCache();
		this.rainfallCache.cleanupCache();
		this.stabilityCache.cleanupCache();
		this.phCache.cleanupCache();
		this.drainageCache.cleanupCache();
		while(worldTempCache.size() > 51000)
		{
			trimTempCache();
		}
	}

	public float getTemp(int x, int z, int totalHours)
	{
		String key = x+","+z+","+totalHours;
		if(worldTempCache != null && worldTempCache.containsKey(key))
		{
			return worldTempCache.get(key);
		}
		return Float.MIN_VALUE;
	}

	public void addTemp(int x, int z, int totalHours, float temp)
	{
		String key = x+","+z+","+totalHours;
		if(worldTempCache != null)
			worldTempCache.put(key, temp);
		trimTempCache();
	}

	private void trimTempCache()
	{
		if(worldTempCache.size() > 50000)
		{
			Iterator iter = worldTempCache.keySet().iterator();
			if(iter.hasNext())
				worldTempCache.remove(iter.next());
		}
	}

	public DataLayer getDataLayerAt(DataCache cache, GenLayerTFC indexLayers, int par1, int par2, int index)
	{
		return cache.getDataLayerAt(indexLayers, par1, par2);
	}

	/*private DataLayer[] loadDataLayerGeneratorData(DataCache[] cache, DataLayer[] layers, GenLayerTFC[] indexLayers, int par2, int par3, int par4, int par5, int layer)
	{
		return this.getDataLayerAt(cache, layers, indexLayers, par2, par3, par4, par5, true, layer);
	}*/

	public DataLayer[] getDataLayerAt(DataCache[] cache, DataLayer[] layers, GenLayerTFC[] indexLayers, int x, int y, int width, int height, boolean par6, int layer)
	{
		if (layers == null || layers.length < width * height)
			layers = new DataLayer[width * height];

		if (par6 && width == 16 && height == 16 && (x & 15) == 0 && (y & 15) == 0)
		{
			DataLayer[] var9 = cache[layer].getCachedData(indexLayers[layer], x, y);
			System.arraycopy(var9, 0, layers, 0, width * height);
			return layers;
		}
		else
		{
			IntCache.resetIntCache();
			int[] var7 = indexLayers[layer].getInts(x, y, width, height);
			for (int var8 = 0; var8 < width * height; ++var8)
			{
				layers[var8] = DataLayer.layers[var7[var8]];
			}
			return layers;
		}
	}

	public DataLayer[] getDataLayerAt(DataCache cache, DataLayer[] layers, GenLayerTFC indexLayers, int x, int y, int width, int height, boolean par6, int layer)
	{
		if (layers == null || layers.length < width * height)
			layers = new DataLayer[width * height];

		if (par6 && width == 16 && height == 16 && (x & 15) == 0 && (y & 15) == 0)
		{
			DataLayer[] var9 = cache.getCachedData(indexLayers, x, y);
			System.arraycopy(var9, 0, layers, 0, width * height);
			return layers;
		}
		else
		{
			IntCache.resetIntCache();
			int[] var7 = indexLayers.getInts(x, y, width, height);
			for (int var8 = 0; var8 < width * height; ++var8)
			{
				layers[var8] = DataLayer.layers[var7[var8]];
			}
			return layers;
		}
	}

	public DataLayer getTopRockLayerAt(int x, int z) {
		return getRockLayerAt(x, z, 0);
	}

	public DataLayer getBottomRockLayerAt(int x, int z) {
		return getRockLayerAt(x, z, ROCK_LAYER_COUNT - 1);
	}

	public DataLayer[] getAllRockLayers(int x, int z) {
		DataLayer[] layers = new DataLayer[ROCK_LAYER_COUNT];
		for (int i = 0; i < layers.length; i++) layers[i] = getRockLayerAt(x, z, i);
		return layers;
	}

	public DataLayer getRockLayerAtHeight(World world, int x, int y, int z) {
		return getRockLayerAt(x, z, TFC_Core.getRockLayerFromHeight(world, x, y, z));
	}

	public static int getRockLayerIndex(int h) {
		int index = ROCK_LAYER_COUNT - 1;
		for (int i = 0; i < ROCK_LAYER_SPLITERATOR.length - 1; i++, index--) {
			if (h > ROCK_LAYER_SPLITERATOR[i] && h <= ROCK_LAYER_SPLITERATOR[i+1]) break;
		}
		return index >= 0 ? index : 0;
	}

	public DataLayer getRockLayerAt(int x, int z, int index)
	{
		return this.rockCache[index].getDataLayerAt(rocksIndexLayer[index], x, z);
	}

	/**
	 * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager Args: oldBiomeList, x, z, width, depth
	 */
	public DataLayer[] loadRockLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height, int layer)
	{
		return this.getDataLayerAt(rockCache.clone(), layers, rocksIndexLayer, x, y, width, height, true, layer);
	}

	public DataLayer getTreeLayerAt(int x, int z, int index)
	{
		return this.treeCache[index].getDataLayerAt(treesIndexLayer[index], x, z);
	}

	/**
	 * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager Args: oldBiomeList, x, z, width, depth
	 */
	public DataLayer[] loadTreeLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height, int layer)
	{
		return this.getDataLayerAt(treeCache.clone(), layers, treesIndexLayer, x, y, width, height, true, 0);
	}

	public DataLayer getEVTLayerAt(int x, int z)
	{
		return this.evtCache.getDataLayerAt(evtIndexLayer, x, z);
	}

	/**
	 * Returns evt map to use for the blocks
	 * Args: layers, x, z, width, depth
	 */
	public DataLayer[] loadEVTLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height)
	{
		return this.getDataLayerAt(evtCache, layers, evtIndexLayer, x, y, width, height, true, 0); //NOPMD
	}

	public DataLayer getRainfallLayerAt(int x, int z)
	{
		return this.rainfallCache.getDataLayerAt(rainfallIndexLayer, x, z);
	}

	/**
	 * Returns rainfall map Args: layers, x, z, width, depth
	 */
	public DataLayer[] loadRainfallLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height)
	{
		return this.getDataLayerAt(rainfallCache, layers, rainfallIndexLayer, x, y, width, height, true, 0); //NOPMD
	}

	public DataLayer getStabilityLayerAt(int x, int z)
	{
		return this.stabilityCache.getDataLayerAt(stabilityIndexLayer, x, z);
	}

	public DataLayer[] loadStabilityLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height)
	{
		return this.getDataLayerAt(stabilityCache, layers, stabilityIndexLayer, x, y, width, height, true, 0); //NOPMD
	}

	public DataLayer getPHLayerAt(int x, int z)
	{
		DataLayer dl = this.phCache.getDataLayerAt(phIndexLayer, x, z);
		return dl != null ? dl : DataLayer.PH_NEUTRAL;
	}

	public DataLayer[] loadPHLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height)
	{
		return this.getDataLayerAt(phCache, layers, phIndexLayer, x, y, width, height, true, 0); //NOPMD
	}

	public DataLayer getDrainageLayerAt(int x, int z)
	{
		return this.drainageCache.getDataLayerAt(drainageIndexLayer, x, z);
	}

	public DataLayer[] loadDrainageLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height)
	{
		return this.getDataLayerAt(drainageCache, layers, drainageIndexLayer, x, y, width, height, true, 0); //NOPMD
	}
}
