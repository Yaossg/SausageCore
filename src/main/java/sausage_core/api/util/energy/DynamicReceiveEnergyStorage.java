package sausage_core.api.util.energy;

public class DynamicReceiveEnergyStorage extends DynamicEnergyStorage {
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return (int) (super.receiveEnergy((int) (maxReceive * getEfficiency()), simulate) / getEfficiency());
	}

	@Override
	protected float getReceiveEfficiency() {
		return getEfficiency();
	}
}
