package com.github.yaossg.sausage_core.proxy;

import com.github.yaossg.sausage_core.SausageCore;
import com.github.yaossg.sausage_core.api.util.IBRegistryManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public static final IBRegistryManager manager = new IBRegistryManager(SausageCore.MODID, CreativeTabs.FOOD);
    public static final Item sausage = manager.addItem(new ItemFood(10, 1f, false) {}, "sausage");
    public void preInit(FMLPreInitializationEvent event) {
        manager.registerAll();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}