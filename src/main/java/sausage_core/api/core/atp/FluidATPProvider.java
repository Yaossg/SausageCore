package sausage_core.api.core.atp;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.function.ToIntFunction;

public class FluidATPProvider implements IATPProvider {
	private final FluidTank tank;
	private final ToIntFunction<FluidStack> function;

	public FluidATPProvider(FluidTank tank, ToIntFunction<FluidStack> function) {
		this.tank = tank;
		this.function = function;
	}

	@Override
	public int provide(int goal) {
		int ret = 0;
		while (ret < goal && tank.drain(1, false) != null)
			ret += function.applyAsInt(tank.drain(1, true));
		return ret;
	}
}
