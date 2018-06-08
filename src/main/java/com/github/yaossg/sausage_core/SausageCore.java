package com.github.yaossg.sausage_core;

import com.github.yaossg.sausage_core.api.util.Utils;
import com.github.yaossg.sausage_core.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = SausageCore.MODID, name = SausageCore.NAME, version = SausageCore.VERSION, acceptedMinecraftVersions = "1.12.2")
public class SausageCore
{
    public static final String MODID = "sausage_core";
    public static final String NAME = "SausageCore";
    public static final String VERSION = "0.1.2";

    @SidedProxy(clientSide = "com.github.yaossg.sausage_core.proxy.ClientProxy",
            serverSide = "com.github.yaossg.sausage_core.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(SausageCore.MODID)
    public static SausageCore instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Utils.unstableWarning(logger = event.getModLog(), NAME, VERSION, MODID);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

}