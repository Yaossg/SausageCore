package yaossg.mod.sausage_core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import yaossg.mod.sausage_core.proxy.CommonProxy;

@Mod(modid = SausageCore.MODID, name = SausageCore.NAME, version = SausageCore.VERSION, acceptedMinecraftVersions = "1.12.2")
public class SausageCore
{
    public static final String MODID = "sausage_core";
    public static final String NAME = "SausageCore";
    public static final String VERSION = "0.1.0";

    @SidedProxy(clientSide = "yaossg.mod.sausage_core.proxy.ClientProxy",
            serverSide = "yaossg.mod.sausage_core.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(SausageCore.MODID)
    public static SausageCore instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info(NAME + " v" + VERSION + " is loading (modid:" + MODID + ")");
        logger.warn("The mod is still unstable and in early development, There are still lots of bugs remaining");
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