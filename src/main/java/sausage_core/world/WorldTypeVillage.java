package sausage_core.world;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import sausage_core.SausageCore;
import sausage_core.api.core.common.WorldTypeModID;

import java.util.Map;

public class WorldTypeVillage extends WorldTypeModID {
    public WorldTypeVillage() {
        super(SausageCore.MODID, "village");
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        ChunkGeneratorFlat flat = new ChunkGeneratorFlat(world, world.getSeed(), true,
                "3;minecraft:bedrock,59*minecraft:stone,3*minecraft:dirt,minecraft:grass;1;" +
                        "biome_1,decoration,stronghold,mineshaft,lake,lava_lake,dungeon");
        Map<String, MapGenStructure> structureGenerators = ReflectionHelper.getPrivateValue(ChunkGeneratorFlat.class, flat, "structureGenerators");
        structureGenerators.put("Village", new MapGenVillage(ImmutableMap.of("size", "65535")) {
            @Override
            protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
                int dist = 10;
                return chunkX == chunkX / dist * dist
                        || chunkZ == chunkZ / dist * dist;
            }
        });
        return flat;
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        net.minecraft.world.gen.FlatGeneratorInfo info = net.minecraft.world.gen.FlatGeneratorInfo.createFlatGeneratorFromString(world.getWorldInfo().getGeneratorOptions());
        return new net.minecraft.world.biome.BiomeProviderSingle(net.minecraft.world.biome.Biome.getBiome(info.getBiome(), net.minecraft.init.Biomes.DEFAULT));
    }
}
