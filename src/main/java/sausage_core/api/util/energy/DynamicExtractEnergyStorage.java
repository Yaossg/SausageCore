package sausage_core.api.util.energy;

public class DynamicExtractEnergyStorage extends DynamicEnergyStorage {
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return (int) (super.extractEnergy((int) (maxExtract / getEfficiency()), simulate) * getEfficiency());
	}

	@Override
	protected float getExtractEfficiency() {
		return getEfficiency();
	}
}
