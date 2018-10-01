package sausage_core.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldTypeChaos extends WorldType {

    public WorldTypeChaos() {
        super("chaos");
    }

    private static final List<Biome> BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
    @Override
    public BiomeProvider getBiomeProvider(World world) {

        return new BiomeProvider() {

            public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
                if (oldBiomeList == null || oldBiomeList.length < width * depth)
                    oldBiomeList = new Biome[width * depth];

                Arrays.fill(oldBiomeList, 0, width * depth, BIOMES.get(Math.abs(new ChunkPos((x - 2) / 32, (z - 2) / 32).hashCode()) % BIOMES.size()));
                return oldBiomeList;
            }

            public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
                return this.getBiomes(listToReuse, x, z, width, length);
            }

            public Biome[] getBiomesForGeneration(@Nullable Biome[] biomes, int x, int z, int width, int height) {
                if (biomes == null || biomes.length < width * height)
                    biomes = new Biome[width * height];
                Arrays.fill(biomes, 0, width * height, BIOMES.get(Math.abs(new ChunkPos((x - 2) / 32, (z - 2) / 32).hashCode()) % BIOMES.size()));
                return biomes;
            }

            @Nullable
            @Override
            public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
                return null;
            }

            public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
                return allowed.contains(getBiome(new ChunkPos(x, z).getBlock(0,0,0)));
            }
        };
    }

    @Override
    public boolean hasInfoNotice() {
        return true;
    }

    @Override
    public String getTranslationKey() {
        return "generator.sausage_core.chaos";
    }

    @Override
    public String getInfoTranslationKey() {
        return "generator.sausage_core.chaos.info";
    }

}
