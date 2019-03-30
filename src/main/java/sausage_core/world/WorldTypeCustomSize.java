package sausage_core.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.SausageCore;
import sausage_core.api.core.common.WorldTypeModID;
import sausage_core.gui.GUICreateCustomSize;

public class WorldTypeCustomSize extends WorldTypeModID {
	public WorldTypeCustomSize() {
		super(SausageCore.MODID, "customSize");
		MinecraftForge.TERRAIN_GEN_BUS.register(this);
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
				GenLayer[] genLayers = GenLayer.initializeAllBiomeGenerators(world.getSeed(), world.getWorldType(),
						ChunkGeneratorSettings.Factory.jsonToFactory(String.format("{\"biomeSize\":%s}",
								world.getWorldInfo().getGeneratorOptions())).build());
				genBiomes = genLayers[0];
				biomeIndexLayer = genLayers[1];
			}
		};
	}
}
