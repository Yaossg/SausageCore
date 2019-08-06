package sausage_core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Logger;
import sausage_core.api.annotation.AutoCall;
import sausage_core.api.annotation.InjectLogger;
import sausage_core.api.core.common.ItemGroup;
import sausage_core.api.registry.AutoSyncConfig;
import sausage_core.api.util.registry.IBRegistryManager;
import sausage_core.command.CommandData;
import sausage_core.impl.SCASMImpl;
import sausage_core.impl.SCFRMImpl;
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
	public static Item sausage, debug_stick;
	public static final IBRegistryManager IB = new IBRegistryManager(MODID,
			new ItemGroup(MODID, () -> new ItemStack(sausage)));

	@InjectLogger
	public static Logger logger;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SCASMImpl.init(event.getAsmData());
		logger.info("{} {} is loading", NAME, VERSION);
		MinecraftForge.EVENT_BUS.register(AutoSyncConfig.class);
		sausage = IB.addItem("sausage", new ItemSausage());
		debug_stick = IB.addItem("debug_stick", new ItemDebugStick());
		new WorldTypeCustomSize();
		new WorldTypeBuffet();
		SCASMImpl.call(AutoCall.When.PRE_INIT);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		SCASMImpl.call(AutoCall.When.INIT);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
		SCFRMImpl.IMPL.load();
		SCASMImpl.call(AutoCall.When.POST_INIT);
	}

	@SubscribeEvent
	public static void loadModels(ModelRegistryEvent event) {
		SCASMImpl.call(AutoCall.When.LOAD_MODEL);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> register) {
		SCASMImpl.call(AutoCall.When.IB_REGISTER);
	}

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