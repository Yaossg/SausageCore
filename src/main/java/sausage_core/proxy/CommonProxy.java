package sausage_core.proxy;

import sausage_core.SausageCore;
import sausage_core.api.util.registry.IBRegistryManager;
import sausage_core.gen.WorldTypeChaos;
import sausage_core.gen.WorldTypeMiniature;
import sausage_core.gen.WorldTypeTinyBiomes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public static final IBRegistryManager manager = new IBRegistryManager(SausageCore.MODID, CreativeTabs.FOOD);
    public static final Item sausage = manager.addItem(new ItemFood(12, 1f, false), "sausage");

    public void preInit(FMLPreInitializationEvent event) {
        manager.registerAll();
        new WorldTypeTinyBiomes();
        new WorldTypeMiniature();
        new WorldTypeChaos();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}