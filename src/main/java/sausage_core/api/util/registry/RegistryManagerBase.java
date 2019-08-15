package sausage_core.api.util.registry;

import net.minecraft.util.ResourceLocation;
import sausage_core.api.annotation.AutoCall;

public abstract class RegistryManagerBase {
	public final String modid;

	protected RegistryManagerBase(String modid, boolean requireAutoCall) {
		this.modid = modid;
		if (requireAutoCall) AutoCall.When.checkState();
	}

	@Deprecated
	public final ResourceLocation newIdentifier(String path) {
		return new ResourceLocation(modid, path);
	}

}
