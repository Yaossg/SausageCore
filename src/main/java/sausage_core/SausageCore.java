package sausage_core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import org.apache.logging.log4j.Logger;
import sausage_core.api.registry.AutoSyncConfigs;
import sausage_core.api.registry.SCFRecipeManager;
import sausage_core.api.util.common.SausageUtils;
import sausage_core.api.util.oredict.OreDicts;
import sausage_core.api.util.registry.IBRegistryManager;
import sausage_core.config.SausageCoreConfig;
import sausage_core.item.ItemDebugStick;
import sausage_core.item.ItemInfoCard;
import sausage_core.item.ItemSausage;
import sausage_core.world.WorldTypeBuffet;
import sausage_core.world.WorldTypeCustomSize;
import sausage_core.world.WorldTypeVillage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        MinecraftForge.EVENT_BUS.register(AutoSyncConfigs.class);
        AutoSyncConfigs.AUTO_SYNC_CONFIG.register(MODID);
        sausage = manager.addItem("sausage", new ItemSausage());
        manager.addItem("info_card", new ItemInfoCard());
        manager.addItem("debug_stick", new ItemDebugStick());
        manager.registerAll();
        new WorldTypeCustomSize();
        new WorldTypeBuffet();
        new WorldTypeVillage();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if(SausageCoreConfig.testAl) {
            OreDictionary.registerOre("ingotAluminum", Items.STICK);
            OreDictionary.registerOre("ingotAluminium", Items.SNOWBALL);

            GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, "aluminum_only"), null, new ItemStack(Items.APPLE, 9),
                    new OreIngredient("ingotAluminum"));
            GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "aluminium_only"), null, new ItemStack(Items.CAKE),
                    "xxx", "xxx", "xxx", 'x', new OreIngredient("ingotAluminium"));
        }
    }

    void registerOres(String from, String to) {
        Arrays.stream(OreDictionary.getOreNames())
                .filter(ore -> OreDicts.materialOf(ore).equals(from))
                .collect(Collectors.toMap(Function.identity(), OreDictionary::getOres))
                .forEach((ore, stacks) -> OreDicts.shapeOf(ore).ifPresent(shape -> stacks.forEach(stack -> OreDictionary.registerOre(shape + to, stack))));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
        registerOres("Aluminum", "Aluminium");
        registerOres("Aluminium", "Aluminum");
        SCFRecipeManager.load();
    }

    @SubscribeEvent
    public static void loadModels(ModelRegistryEvent event) {
        manager.loadAllModel();
    }

}