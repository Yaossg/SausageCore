package sausage_core.api.core.plugin;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public abstract class PluginTConstructCore extends PluginCore {
	public PluginTConstructCore() {
		super("tconstruct");
	}

	protected static Fluid addMoltenFluid(String fluidName, int color, int temperature) {
		Fluid fluid = new Fluid(fluidName,
				new ResourceLocation("tconstruct:blocks/fluids/molten_metal"),
				new ResourceLocation("tconstruct:blocks/fluids/molten_metal_flow"))
				.setColor(color).setTemperature(temperature);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	protected static Fluid get(String fluid) {
		return FluidRegistry.getFluid(fluid);
	}
}
