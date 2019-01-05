package sausage_core.api.util.energy;

public class DynamicExtractEnergyStorage extends DynamicEnergyStorage {
    public DynamicExtractEnergyStorage(int capacity) {
        super(capacity);
    }

    public DynamicExtractEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public DynamicExtractEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public DynamicExtractEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return (int) (super.extractEnergy((int) (maxExtract * getEfficiency()), simulate) / getEfficiency());
    }

    @Override
    protected float getExtractEfficiency() {
        return getEfficiency();
    }
}
