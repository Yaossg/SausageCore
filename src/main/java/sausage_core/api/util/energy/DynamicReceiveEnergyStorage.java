package sausage_core.api.util.energy;

public class DynamicReceiveEnergyStorage extends DynamicEnergyStorage {
    public DynamicReceiveEnergyStorage(int capacity) {
        super(capacity);
    }

    public DynamicReceiveEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public DynamicReceiveEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public DynamicReceiveEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) (super.receiveEnergy((int) (maxReceive * getEfficiency()), simulate) / getEfficiency());
    }

    @Override
    protected float getReceiveEfficiency() {
        return getEfficiency();
    }
}
