package sausage_core.api.core.atp;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyATPProvider implements IATPProvider {
	private final IEnergyStorage storage;

	public EnergyATPProvider(IEnergyStorage storage) {
		this.storage = storage;
	}

	@Override
	public int provide(int goal) {
		return storage.extractEnergy(goal, false);
	}
}
