package sausage_core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import sausage_core.api.registry.AutoSyncConfig;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.registry.IBRegistryManager;
import sausage_core.command.CommandData;
import sausage_core.impl.SCFRecipeManagerImpl;
import sausage_core.item.ItemDebugStick;
import sausage_core.item.ItemSausage;
import sausage_core.world.WorldTypeBuffet;
import sausage_core.world.WorldTypeCustomSize;

import java.util.ArrayList;

/**
 * @author Yaossg
 */
@Mod(modid = SausageCore.MODID,
		name = SausageCore.NAME,
		version = SausageCore.VERSION,
		acceptedMinecraftVersions = "1.12.2",
		dependencies = "required-after:guideapi")
@Mod.EventBusSubscriber
public class SausageCore {
	public static final String MODID = "sausage_core";
	public static final String NAME = "SausageCore";
	public static final String VERSION = "@version@";
	public static Logger logger;
	public static final IBRegistryManager IB = new IBRegistryManager(MODID, new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(sausage);
		}
	});
	public static Item sausage, debug_stick;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		SausageUtils.loadingInformation(NAME, VERSION, MODID);
		MinecraftForge.EVENT_BUS.register(AutoSyncConfig.class);
		sausage = IB.addItem("sausage", new ItemSausage());
		debug_stick = IB.addItem("debug_stick", new ItemDebugStick());
		IB.registerAll();
		new WorldTypeCustomSize();
		new WorldTypeBuffet();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
		SCFRecipeManagerImpl.IMPL.load();
	}

	@SubscribeEvent
	public static void loadModels(ModelRegistryEvent event) {
		IB.loadAllModel();
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class RenderSubscriber {
		/***
		 * Fix "BUG" by forge, here are the differences:
		 * Vanilla: entity == null || !(entity instanceof EntityLivingBase)
		 * Forge: mc.player.getRidingEntity() == null
		 */
		@SubscribeEvent
		public static void onRender(RenderGameOverlayEvent.Pre event) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
				GuiIngameForge.renderFood = !GuiIngameForge.renderHealthMount;
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandData());
	}

	@SubscribeEvent
	public static void onMissingMappings(RegistryEvent.MissingMappings<Item> event) {
		for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings())
			if (entry.key.toString().equals("sausage_core:info_card")) entry.ignore();
	}
}