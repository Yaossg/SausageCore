package sausage_core.proxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sausage_core.SausageCore;
import sausage_core.api.util.registry.IBRegistryManager;
import sausage_core.item.ItemSausage;
import sausage_core.world.WorldTypeBuffet;
import sausage_core.world.WorldTypeCustomSize;

import java.util.ArrayList;

public class CommonProxy {
    public static final IBRegistryManager manager = new IBRegistryManager(SausageCore.MODID, CreativeTabs.FOOD);
    public static final Item sausage = manager.addItem(new ItemSausage(), "sausage");

    public void preInit(FMLPreInitializationEvent event) {
        manager.registerAll();
        new WorldTypeCustomSize();
        new WorldTypeBuffet();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        WorldTypeBuffet.BIOMES = new ArrayList<>(ForgeRegistries.BIOMES.getValuesCollection());
    }

}