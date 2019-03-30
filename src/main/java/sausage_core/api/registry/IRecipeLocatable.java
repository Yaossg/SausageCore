package sausage_core.api.registry;

import net.minecraft.util.ResourceLocation;

public interface IRecipeLocatable {
	ResourceLocation getLocation();

	void setLocation(ResourceLocation location);
}
