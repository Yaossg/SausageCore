package com.github.yaossg.sausage_core.api.util.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * delegates {@link IWorldGenerator}
 * @deprecated
 */
@Deprecated
public interface IWorldGenBiome extends IWorldGenerator {
    @Override
    default void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider,
                world.getBiomeProvider().getBiome(new BlockPos(chunkX << 4, 64, chunkZ << 4)));
    }

    static BlockPos randomPos(Random random, int chunkX, int chunkZ, int y) {
        return new BlockPos((chunkX << 4) + random.nextInt(16), y, (chunkZ << 4) + random.nextInt(16));
    }

    static BlockPos randomPos(Random random, int chunkX, int chunkZ, int minY, int maxY) {
        return randomPos(random, chunkX, chunkZ, random.nextInt(maxY - minY) + minY);
    }

    void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Biome biome);

}
