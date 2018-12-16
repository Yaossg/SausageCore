package sausage_core.world;

import com.google.common.math.IntMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import sausage_core.util.WorldTypeUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldTypeCrazyBiomes extends WorldType {

    public WorldTypeCrazyBiomes() {
        super("crazyBiomes");
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return new CrazyBiomeProvider();
    }

    @Override
    public String getTranslationKey() {
        return "generator.sausage_core.crazyBiomes";
    }

    @Override
    public boolean isCustomizable() {
        return true;
    }

    @Override
    public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {

    }

    public enum Pattern {
        CHESSBOARD("chessboard"),
        LINE_X("x=k"), LINE_Z("z=k"),
        LINE_XZ("x=z"), LINE_ZX("x=-z"),
        CROSS("x=k, z=k"), CROSS_XZ("x=z, x=-z"),
        RING("x*x+z*z=r*r");
        String desc;
        Pattern(String desc) {
            this.desc = desc;
        }
    }


    public static class CrazyBiomeConfig {
        public int scale;
        public CrazyBiomeConfig(int scale) {
            this.scale = scale;
        }
        public int hash(int x, int z) {
            long chunkID = WorldTypeUtils.chunkID_offset(x, z);
            chunkID = WorldTypeUtils.scale(chunkID, scale);
            return WorldTypeUtils.hashChunk(chunkID);
        }
    }

    public static class CrazyBiomeProvider extends BiomeProvider {

        public static CrazyBiomeConfig crazyBiomeConfig = new CrazyBiomeConfig(4);
        public static Biome get(int x, int z) {
            return WorldTypeBuffet.BIOMES.get(Math.abs(crazyBiomeConfig.hash(x, z)) % WorldTypeBuffet.BIOMES.size());
        }

        public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
            if (oldBiomeList == null || oldBiomeList.length < width * depth)
                oldBiomeList = new Biome[width * depth];

            Arrays.fill(oldBiomeList, 0, width * depth, get(x, z));
            return oldBiomeList;
        }

        public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
            return this.getBiomes(listToReuse, x, z, width, length);
        }

        public Biome[] getBiomesForGeneration(@Nullable Biome[] biomes, int x, int z, int width, int height) {
            if (biomes == null || biomes.length < width * height)
                biomes = new Biome[width * height];
            Arrays.fill(biomes, 0, width * height, get(x, z));
            return biomes;
        }

        @Nullable
        @Override
        public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
            return null;
        }

        public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
            return allowed.contains(getBiome(new BlockPos((x << 4) + x, 0, (z << 4) + z)));
        }
    }
}
