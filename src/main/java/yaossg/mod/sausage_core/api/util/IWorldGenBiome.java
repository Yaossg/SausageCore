package yaossg.mod.sausage_core.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public interface IWorldGenBiome extends IWorldGenerator {
    @Override
    default void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider,
                world.getBiomeProvider().getBiome(new BlockPos(chunkX << 4, 64, chunkZ << 4)));
    }

    void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, Biome biome);

}
