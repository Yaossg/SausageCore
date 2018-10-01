package sausage_core.api.util.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public final class WorldGenUtils {
    public static BlockPos randomPos(Random random, int chunkX, int chunkZ, int y) {
        return new BlockPos((chunkX << 4) + random.nextInt(16), y, (chunkZ << 4) + random.nextInt(16));
    }

    public static BlockPos randomPos(Random random, int chunkX, int chunkZ, int minY, int maxY) {
        return randomPos(random, chunkX, chunkZ, random.nextInt(maxY - minY) + minY);
    }

    public static Biome getBiome(World world, int chunkX, int chunkZ) {
        return world.getBiomeProvider().getBiome(new BlockPos(chunkX << 4, 0, chunkZ << 4));
    }
}
