package sausage_core.api.registry;

import net.minecraft.util.ResourceLocation;

public abstract class RecipeLocatable implements IRecipeLocatable {
	protected ResourceLocation location;

	@Override
	public ResourceLocation getLocation() {
		return location;
	}

	@Override
	public void setLocation(ResourceLocation location) {
		this.location = location;
	}
}
