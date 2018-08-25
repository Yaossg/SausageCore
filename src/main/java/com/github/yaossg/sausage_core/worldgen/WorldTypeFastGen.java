package com.github.yaossg.sausage_core.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeFastGen extends WorldType {
    public WorldTypeFastGen() {
        super("FastGen");

    }
    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        return new ChunkGenFast(world, world.getSeed(), new FastGenSettings(), generatorOptions);
    }
    class FastGenSettings {
        public boolean forgeHooksEnabled = false;
        public boolean flattenTerrain = true;
    }
}
