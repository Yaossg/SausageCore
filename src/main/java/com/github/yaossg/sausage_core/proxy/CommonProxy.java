package com.github.yaossg.sausage_core.proxy;

import com.github.yaossg.sausage_core.SausageCore;
import com.github.yaossg.sausage_core.api.util.registry.IBRegistryManager;
import com.github.yaossg.sausage_core.world.gen.WorldTypeMiniature;
import com.github.yaossg.sausage_core.world.gen.WorldTypeTinyBiomes;
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
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}