package com.github.yaossg.sausage_core.api.util.common;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;

@MethodsReturnNonnullByDefault
public class Explosions {
    public static Explosion apply(World world, Explosion explosion) {
        return apply(world, explosion, true);
    }

    public static Explosion apply(World world, Explosion explosion, boolean spawnParticles) {
        if(!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) {
            explosion.doExplosionA();
            explosion.doExplosionB(spawnParticles);
        }
        return explosion;
    }

    public static Explosion createToApply(World world, Entity entity, BlockPos pos,
                                          float size, boolean flaming, boolean damagesTerrain) {
        return createToApply(world, entity, pos, size, flaming, damagesTerrain, true);
    }

    public static Explosion createToApply(World world, Entity entity, BlockPos pos,
                                          float size, boolean flaming, boolean damagesTerrain, List<BlockPos> affectedPositions) {
        return createToApply(world, entity, pos, size, flaming, damagesTerrain, affectedPositions, true);
    }

    public static Explosion createToApply(World world, Entity entity, BlockPos pos,
                                          float size, boolean flaming, boolean damagesTerrain, boolean spawnParticles) {
        return apply(world, new Explosion(world, entity, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                size, flaming, damagesTerrain), spawnParticles);
    }

    public static Explosion createToApply(World world, Entity entity, BlockPos pos,
                                          float size, boolean flaming, boolean damagesTerrain, List<BlockPos> affectedPositions, boolean spawnParticles) {
        return apply(world, new Explosion(world, entity, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                size, flaming, damagesTerrain, affectedPositions), spawnParticles);
    }
}
