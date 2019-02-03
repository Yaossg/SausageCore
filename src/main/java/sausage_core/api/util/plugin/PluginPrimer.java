package sausage_core.api.util.plugin;


import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sausage_core.api.core.plugin.PluginCore;

public final class PluginPrimer {
    Multimap<String, String> plugins = MultimapBuilder.hashKeys().hashSetValues().build();
    private static Logger logger = LogManager.getLogger("PluginPrimer");

    public void register(String modid, String plugin) {
        plugins.put(modid, plugin);
    }

    public void execute() {
        plugins.asMap().forEach((modid, plugins) -> {
            if(Loader.isModLoaded(modid))
                for(String plugin : plugins)
                    execute(modid, plugin);
        });
    }

    void execute(String modid, String plugin) {
        PluginCore pluginCore;
        try {
            pluginCore = (PluginCore) Class.forName(plugin).newInstance();
        } catch(Exception e) {
            logger.error("Failed to create an instance of plugin for " + modid, e);
            return;
        }
        String pluginModid = pluginCore.get();
        if(!pluginModid.equals(modid)) {
            logger.warn("The plugin({})'s modid({}) is different from what it was registered({})", plugin, pluginModid, modid);
            return;
        }
        pluginCore.run();
    }
}
