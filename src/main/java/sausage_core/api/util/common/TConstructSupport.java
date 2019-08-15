package sausage_core.api.util.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TConstructSupport {
	private TConstructSupport() {}
	public static Fluid addMolten(String fluidName, int color, int temperature) {
		Fluid fluid = new Fluid(fluidName,
				new ResourceLocation("tconstruct:blocks/fluids/molten_metal"),
				new ResourceLocation("tconstruct:blocks/fluids/molten_metal_flow"))
				.setColor(color).setTemperature(temperature);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	public static Fluid get(String name) {
		return FluidRegistry.getFluid(name);
	}
}
