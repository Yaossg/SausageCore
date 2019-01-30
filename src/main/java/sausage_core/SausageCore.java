package sausage_core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Logger;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.registry.IBRegistryManager;
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
    public static final Item sausage = manager.addItem(new ItemSausage(), "sausage");
    public static final Item info_card = manager.addItem(new ItemInfoCard(), "info_card");

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return 0;
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SausageUtils.unstableWarning(logger = event.getModLog(), NAME, VERSION, MODID);
        manager.registerAll();
        new WorldTypeCustomSize();
        new WorldTypeBuffet();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
    }

    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event) {
        manager.loadAllModel();
    }

}