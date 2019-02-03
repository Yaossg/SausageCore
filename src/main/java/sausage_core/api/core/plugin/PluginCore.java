package sausage_core.api.core.plugin;

import java.util.function.Supplier;

public abstract class PluginCore implements Runnable, Supplier<String> {
    protected String modid;
    protected PluginCore(String modid) {
        this.modid = modid;
    }

    @Override
    public String get() {
        return modid;
    }
}
