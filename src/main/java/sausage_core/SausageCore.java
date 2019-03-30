package sausage_core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import sausage_core.api.registry.AutoSyncConfig;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.oredict.OreDicts;
import sausage_core.api.util.registry.IBRegistryManager;
import sausage_core.impl.SCFRecipeManagerImpl;
import sausage_core.item.ItemDebugStick;
import sausage_core.item.ItemInfoCard;
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
		acceptedMinecraftVersions = "1.12.2")
@Mod.EventBusSubscriber
public class SausageCore {
	public static final String MODID = "sausage_core";
	public static final String NAME = "SausageCore";
	public static final String VERSION = "@version@";
	public static Logger logger;
	public static final IBRegistryManager manager = new IBRegistryManager(MODID, new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(sausage);
		}
	});
	public static Item sausage;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		SausageUtils.unstableWarning(NAME, VERSION, MODID);
		MinecraftForge.EVENT_BUS.register(AutoSyncConfig.class);
		AutoSyncConfig.AUTO_SYNC_CONFIG.register(MODID);
		sausage = manager.addItem("sausage", new ItemSausage());
		manager.addItem("info_card", new ItemInfoCard());
		manager.addItem("debug_stick", new ItemDebugStick());
		manager.registerAll();
		new WorldTypeCustomSize();
		new WorldTypeBuffet();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
		SCFRecipeManagerImpl.IMPL.load();
	}

	@SubscribeEvent
	public static void loadModels(ModelRegistryEvent event) {
		manager.loadAllModel();
	}

	/**
	 * fix problem of different ore names for Al
	 */
	@SubscribeEvent
	public static void onRegisterOre(OreDictionary.OreRegisterEvent event) {
		String ore = event.getName();
		String material = OreDicts.materialOf(ore);
		switch(material) {
			case "Aluminum":
				OreDicts.shapeOf(ore).ifPresent(shape -> {
					String name = shape + "Aluminium";
					if(OreDicts.names(event.getOre()).noneMatch(name::equals))
						OreDictionary.registerOre(name, event.getOre());
				});
				break;
			case "Aluminium":
				OreDicts.shapeOf(ore).ifPresent(shape -> {
					String name = shape + "Aluminum";
					if(OreDicts.names(event.getOre()).noneMatch(name::equals))
						OreDictionary.registerOre(name, event.getOre());
				});
		}
	}

	@SideOnly(Side.CLIENT)
	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class RenderSubscriber {
		/***
		 * fix bug by forge, here are the differences:
		 * vanilla: entity == null || !(entity instanceof EntityLivingBase)
		 * forge: mc.player.getRidingEntity() == null
		 */
		@SubscribeEvent
		public static void onRender(RenderGameOverlayEvent.Pre event) {
			if(event.getType() == RenderGameOverlayEvent.ElementType.ALL)
				GuiIngameForge.renderFood = !GuiIngameForge.renderHealthMount;
		}
	}
}