package com.github.yaossg.sausage_core.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;

public class Explosions {
    public static void apply(World world, Explosion explosion) {
        apply(world, explosion, true);
    }

    public static void apply(World world, Explosion explosion, boolean spawnParticles) {
        if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) {
            explosion.doExplosionA();
            explosion.doExplosionB(spawnParticles);
        }
    }

    public static void createThenApply(World world, Entity entity, BlockPos pos,
                                       float size, boolean flaming, boolean damagesTerrain) {
        createThenApply(world, entity, pos, size, flaming, damagesTerrain, true);
    }

    public static void createThenApply(World world, Entity entity, BlockPos pos,
                                       float size, boolean flaming, boolean damagesTerrain, List<BlockPos> affectedPositions) {
        createThenApply(world, entity, pos, size, flaming, damagesTerrain, affectedPositions, true);
    }

    public static void createThenApply(World world, Entity entity, BlockPos pos,
                                       float size, boolean flaming, boolean damagesTerrain, boolean spawnParticles) {
        apply(world, new Explosion(world, entity, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                size, flaming, damagesTerrain), spawnParticles);
    }

    public static void createThenApply(World world, Entity entity, BlockPos pos,
                                       float size, boolean flaming, boolean damagesTerrain, List<BlockPos> affectedPositions, boolean spawnParticles) {
        apply(world, new Explosion(world, entity, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                size, flaming, damagesTerrain, affectedPositions), spawnParticles);
    }
}
