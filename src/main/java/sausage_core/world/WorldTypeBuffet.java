package sausage_core.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import sausage_core.SausageCore;
import sausage_core.gui.GUICreateBuffetWorld;

import java.util.List;

public class WorldTypeBuffet extends WorldType {

    public static List<Biome> BIOMES;

    public WorldTypeBuffet() {
        super("buffet");
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return new BiomeProviderSingle(BIOMES.get(SausageCore.parseInt(world.getWorldInfo().getGeneratorOptions())));
    }

    @Override
    public String getTranslationKey() {
        return "generator.sausage_core.buffet";
    }

    @Override
    public String getInfoTranslationKey() {
        return "generator.sausage_core.chaos.buffet";
    }

    @Override
    public boolean isCustomizable() {
        return true;
    }

    @Override
    public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
        mc.displayGuiScreen(new GUICreateBuffetWorld(guiCreateWorld));
    }
}
