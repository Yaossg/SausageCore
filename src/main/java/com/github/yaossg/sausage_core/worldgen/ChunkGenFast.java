package com.github.yaossg.sausage_core.worldgen;

import com.github.yaossg.sausage_core.api.util.math.BufferedRandom;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/*
 * copy from net.minecraft.world.gen.ChunkGeneratorOverworld but faster
 * */
class ChunkGenFast implements IChunkGenerator {

    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private final Random rand;
    private NoiseGeneratorOctaves minLimitPerlinNoise;
    private NoiseGeneratorOctaves maxLimitPerlinNoise;
    private NoiseGeneratorOctaves mainPerlinNoise;
    private NoiseGeneratorPerlin surfaceNoise;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    public NoiseGeneratorOctaves forestNoise;
    private final World world;
    private final double[] heightMap;
    private final float[] biomeWeights;
    private final WorldTypeFastGen.FastGenSettings settings;
    private ChunkGeneratorSettings settings_vanilla;
    private IBlockState oceanBlock = Blocks.WATER.getDefaultState();
    private double[] depthBuffer = new double[256];
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenBase ravineGenerator = new MapGenRavine();
    private Biome[] biomesForGeneration;
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;

    public ChunkGenFast(World worldIn, long seed, WorldTypeFastGen.FastGenSettings settings, String generatorOptions) {
        if(settings.forgeHooksEnabled) {
            caveGenerator = net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(caveGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE);
            ravineGenerator = net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(ravineGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE);
        }
        this.world = worldIn;
        this.rand = BufferedRandom.boxed(new Random(seed)).setLarge(true);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];
        this.settings = settings;
        for (int i = -2; i <= 2; ++i)
            for (int j = -2; j <= 2; ++j) {
                float f = (float) (10 * MathHelper.fastInvSqrt((float) (i * i + j * j) + 0.2F));
                this.biomeWeights[i + 2 + (j + 2) * 5] = f;
            }

        this.settings_vanilla = ChunkGeneratorSettings.Factory.jsonToFactory(generatorOptions).build();
        if(settings.forgeHooksEnabled) {
            net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld ctx =
                    new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld(minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise, depthNoise, forestNoise);
            ctx = net.minecraftforge.event.terraingen.TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctx);
            this.minLimitPerlinNoise = ctx.getLPerlin1();
            this.maxLimitPerlinNoise = ctx.getLPerlin2();
            this.mainPerlinNoise = ctx.getPerlin();
            this.surfaceNoise = ctx.getHeight();
            this.scaleNoise = ctx.getScale();
            this.depthNoise = ctx.getDepth();
            this.forestNoise = ctx.getForest();
        }
    }

    public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, (x << 2) - 2, (z << 2) - 2, 10, 10);
        this.generateHeightmap(x * 4, z * 4);

        for (int i = 0; i < 4; ++i) {
            int j = i * 5;
            int k = (i + 1) * 5;

            for (int l = 0; l < 4; ++l) {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;

                for (int i2 = 0; i2 < 32; ++i2) {
                    double d0 = 0.125D;
                    double d1 = this.heightMap[i1 + i2];
                    double d2 = this.heightMap[j1 + i2];
                    double d3 = this.heightMap[k1 + i2];
                    double d4 = this.heightMap[l1 + i2];
                    double d5 = (this.heightMap[i1 + i2 + 1] - d1) * 0.125D;
                    double d6 = (this.heightMap[j1 + i2 + 1] - d2) * 0.125D;
                    double d7 = (this.heightMap[k1 + i2 + 1] - d3) * 0.125D;
                    double d8 = (this.heightMap[l1 + i2 + 1] - d4) * 0.125D;
                    for (int j2 = 0; j2 < 8; ++j2) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;
                        for (int k2 = 0; k2 < 4; ++k2) {
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * 0.25D;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2)
                                if((lvt_45_1_ += d16) > 0.0D)
                                    primer.setBlockState((i << 2) + k2, (i2 << 3) + j2, (l << 2) + l2, STONE);
                                else if(i2 * 8 + j2 < this.settings_vanilla.seaLevel)
                                    primer.setBlockState((i << 2) + k2, (i2 << 3) + j2, (l << 2) + l2, this.oceanBlock);
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn) {
        if(settings.forgeHooksEnabled)if(!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) return;
        double d0 = 0.03125D;
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double) (x * 16), (double) (z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int i = 0; i < 16; ++i)
            for (int j = 0; j < 16; ++j) {
                Biome biome = biomesIn[j + i * 16];
                biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
    }

    public Chunk generateChunk(int x, int z) {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x << 4, z << 4, 16, 16);
        this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);
        if(!settings.flattenTerrain)
        if(this.settings_vanilla.useCaves) this.caveGenerator.generate(this.world, x, z, chunkprimer);
        if(!settings.flattenTerrain)
        if(this.settings_vanilla.useRavines) this.ravineGenerator.generate(this.world, x, z, chunkprimer);

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i) abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);

        chunk.generateSkylightMap();
        return chunk;
    }

    private void generateHeightmap(int p_185978_1_, int p_185978_3_) {
        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, p_185978_1_, p_185978_3_, 5, 5, (double) this.settings_vanilla.depthNoiseScaleX, (double) this.settings_vanilla.depthNoiseScaleZ, (double) this.settings_vanilla.depthNoiseScaleExponent);
        float f = this.settings_vanilla.coordinateScale;
        float f1 = this.settings_vanilla.heightScale;
        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, p_185978_1_, 0, p_185978_3_, 5, 33, 5, (double) (f / this.settings_vanilla.mainNoiseScaleX), (double) (f1 / this.settings_vanilla.mainNoiseScaleY), (double) (f / this.settings_vanilla.mainNoiseScaleZ));
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, p_185978_1_, 0, p_185978_3_, 5, 33, 5, (double) f, (double) f1, (double) f);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, p_185978_1_, 0, p_185978_3_, 5, 33, 5, (double) f, (double) f1, (double) f);
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
            for (int l = 0; l < 5; ++l) {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                int i1 = 2;
                Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];

                for (int j1 = -2; j1 <= 2; ++j1)
                    for (int k1 = -2; k1 <= 2; ++k1) {
                        Biome biome1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                        float f5 = this.settings_vanilla.biomeDepthOffSet + biome1.getBaseHeight() * this.settings_vanilla.biomeDepthWeight;
                        float f6 = this.settings_vanilla.biomeScaleOffset + biome1.getHeightVariation() * this.settings_vanilla.biomeScaleWeight;
                        if(settings.flattenTerrain && f5 > 0) {
                            f5 *= 0.64f;
                            f6 *= 0.08f;
                        }
                        float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if(biome1.getBaseHeight() > biome.getBaseHeight()) f7 /= 2.0F;

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.depthRegion[j] / 8000.0D;

                if(d7 < 0.0D) d7 = -d7 * 0.3D;

                d7 = d7 * 3.0D - 2.0D;

                if(d7 < 0.0D) {
                    d7 /= 2.0D;
                    if(d7 < -1.0D) d7 = -1.0D;
                    d7 /= 2.8D;
                } else {
                    if(d7 > 1.0D) d7 = 1.0D;
                    d7 /= 8.0D;
                }
                ++j;
                double d8 = (double) f3;
                double d9 = (double) f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * (double) this.settings_vanilla.baseSize / 8.0D;
                double d0 = (double) this.settings_vanilla.baseSize + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1) {
                    double d1 = ((double) l1 - d0) * (double) this.settings_vanilla.stretchY * 0.5 / d9;

                    if(d1 < 0.0D) d1 *= 4.0D;

                    double d2 = this.minLimitRegion[i] / (double) this.settings_vanilla.lowerLimitScale;
                    double d3 = this.maxLimitRegion[i] / (double) this.settings_vanilla.upperLimitScale;
                    double d4 = this.mainNoiseRegion[i] / 20.0D + 0.5D;
                    double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

                    if(l1 > 29) {
                        double d6 = (double) (l1 - 29) / 3;
                        d5 = d5 * (1.0D - d6) -10.0D * d6;
                    }
                    this.heightMap[i] = d5;
                    ++i;
                }
            }
    }

    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        int i = x << 4;
        int j = z << 4;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() | 1L;
        long l = this.rand.nextLong() | 1L;
        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
        boolean flag = false;
        ChunkPos chunkpos = new ChunkPos(x, z);
        if(settings.forgeHooksEnabled)
        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, false);

        if(this.settings_vanilla.useWaterLakes && this.rand.nextInt(this.settings_vanilla.waterLakeChance) == 0)
            if(!settings.forgeHooksEnabled || net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
                int i1 = this.rand.nextInt(16) + 8;
                int j1 = this.rand.nextInt(256);
                int k1 = this.rand.nextInt(16) + 8;
                (new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, blockpos.add(i1, j1, k1));
            }

        if(this.rand.nextInt(this.settings_vanilla.lavaLakeChance / 10) == 0 && this.settings_vanilla.useLavaLakes)
            if(!settings.forgeHooksEnabled || net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA)) {
                int i2 = this.rand.nextInt(16) + 8;
                int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
                int k3 = this.rand.nextInt(16) + 8;

                if(l2 < this.world.getSeaLevel() || this.rand.nextInt(this.settings_vanilla.lavaLakeChance / 8) == 0)
                    (new WorldGenLakes(Blocks.LAVA)).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
            }

        if(this.settings_vanilla.useDungeons)
            if(!settings.forgeHooksEnabled || net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON))
                for (int j2 = 0; j2 < this.settings_vanilla.dungeonChance; ++j2) {
                    int i3 = this.rand.nextInt(16) + 8;
                    int l3 = this.rand.nextInt(256);
                    int l1 = this.rand.nextInt(16) + 8;
                    (new WorldGenDungeons()).generate(this.world, this.rand, blockpos.add(i3, l3, l1));
                }

        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        if(!settings.forgeHooksEnabled || net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
            WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);

        if(settings.forgeHooksEnabled)
        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

        BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return world.getBiome(pos).getSpawnableList(creatureType);
    }

    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }

    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

}
