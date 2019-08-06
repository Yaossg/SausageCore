package sausage_core.api.util.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public final class WorldGenUtils {
	public static BlockPos randomPos(Random random, int chunkX, int chunkZ, int y) {
		return new BlockPos((chunkX << 4) + random.nextInt(16), y, (chunkZ << 4) + random.nextInt(16));
	}

	public static BlockPos randomPos(Random random, int chunkX, int chunkZ, int minY, int maxY) {
		return randomPos(random, chunkX, chunkZ, MathHelper.getInt(random, minY, maxY));
	}

	public static BlockPos commonOffset(Random random, BlockPos pos) {
		return pos.add(random.nextInt(16), 0, random.nextInt(16));
	}
}
