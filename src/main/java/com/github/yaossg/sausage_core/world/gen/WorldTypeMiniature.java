package com.github.yaossg.sausage_core.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeMiniature extends WorldType {
    public WorldTypeMiniature() {
        super("miniature");
    }

    @Override
    public String getTranslationKey() {
        return "generator.sausage_core.miniature";
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        assert generatorOptions.isEmpty();
        return new ChunkGeneratorOverworld(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(),
                "{" +   "\"biomeDepthWeight\": 0, " +
                        "\"biomeScaleWeight\": 0, " +
                        "\"mainNoiseScaleY\": 0" +
                        "\"stretchY\": 50" +
                        "\"depthNoiseScaleExponent\": 0" +
                        "\"heightScale\": 0" +  "}");
    }
}
