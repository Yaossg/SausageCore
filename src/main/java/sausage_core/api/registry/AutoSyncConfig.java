package sausage_core.api.registry;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sausage_core.api.util.registry.SimpleRegistry;

public class AutoSyncConfig {
    public static final SimpleRegistry<String> AUTO_SYNC_CONFIG = new SimpleRegistry<>(String.class);
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(AUTO_SYNC_CONFIG.contains(event.getModID()::equals))
            ConfigManager.sync(event.getModID(), Config.Type.INSTANCE);
    }

}
