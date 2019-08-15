package sausage_core.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.SausageCore;
import sausage_core.api.annotation.LoadClass;
import sausage_core.api.core.common.WorldTypeModID;
import sausage_core.api.util.common.Opts;
import sausage_core.gui.GUICreateBuffetWorld;

import java.util.ArrayList;
import java.util.List;

@LoadClass(construct = true, when = LoadClass.When.INIT)
public class WorldTypeBuffet extends WorldTypeModID {
	public static List<Biome> BIOMES;

	public WorldTypeBuffet() {
		super(SausageCore.MODID, "buffet");
		WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderSingle(BIOMES.get(Opts.parseInt(world.getWorldInfo().getGeneratorOptions()).orElse(0)));
	}

	@Override
	public boolean isCustomizable() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
		mc.displayGuiScreen(new GUICreateBuffetWorld(guiCreateWorld));
	}
}
