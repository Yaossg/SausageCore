package sausage_core.api.core.plugin;

import java.util.function.Supplier;

@Deprecated
public abstract class PluginCore implements Runnable, Supplier<String> {
	protected final String modid;

	protected PluginCore(String modid) {
		this.modid = modid;
	}

	@Override
	public String get() {
		return modid;
	}
}
