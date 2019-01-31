package sausage_core.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.gui.GUICreateCustomSize;

public class WorldTypeCustomSize extends WorldType {

    public WorldTypeCustomSize() {
        super("customSize");
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }

    @Override
    public String getTranslationKey() {
        return "generator.sausage_core.customSize";
    }

    @Override
    public boolean isCustomizable() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
        mc.displayGuiScreen(new GUICreateCustomSize(guiCreateWorld));
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return new BiomeProvider(world.getWorldInfo()) {
            {
                GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(world.getSeed(), world.getWorldType(),
                        ChunkGeneratorSettings.Factory.jsonToFactory(String.format("{\"biomeSize\":%s}",
                                world.getWorldInfo().getGeneratorOptions())).build());
                genBiomes = agenlayer[0];
                biomeIndexLayer = agenlayer[1];
            }
        };
    }
}
