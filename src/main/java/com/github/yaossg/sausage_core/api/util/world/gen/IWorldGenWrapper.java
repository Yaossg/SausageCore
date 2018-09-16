package com.github.yaossg.sausage_core.api.util.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

@FunctionalInterface
public interface IWorldGenWrapper {
    void generate(Random random, World world, BlockPos pos);

    static IWorldGenWrapper of(WorldGenerator generator) {
        return (random, world, pos) -> generator.generate(world, random, pos);
    }

    default WorldGenerator toWorldGenerator() {
        return new WorldGenerator() {
            @Override
            public boolean generate(World worldIn, Random rand, BlockPos position) {
                IWorldGenWrapper.this.generate(rand, worldIn, position);
                return true;
            }
        };
    }
    default IWorldGenerator toIWorldGenerator() {
        return (random, chunkX, chunkZ, world, chunkGenerator, chunkProvider) ->
                generate(random, world, new BlockPos((chunkX << 4), 0, (chunkZ << 4)));
    }
}
