package sausage_core.api.util.energy;

import net.minecraftforge.energy.EnergyStorage;

public class BasicEnergyStorage extends EnergyStorage implements IEnergyModifiable, IEnergyView {
    public BasicEnergyStorage(int capacity) {
        super(capacity);
    }

    public BasicEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public BasicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public BasicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public void setEnergyStored(int value) {
        int old_energy = energy;
        energy = value;
        internalEnergyChange(energy - old_energy);
    }

    @Override
    public void setMaxEnergyStored(int value) {
        capacity = value;
        int old_energy = energy;
        energy = Math.min(energy, capacity);
        internalEnergyChange(energy - old_energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if(!simulate)
            internalEnergyChange(receiveEnergy);
        return receiveEnergy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractEnergy = super.extractEnergy(maxExtract, simulate);
        if(!simulate)
            internalEnergyChange(-extractEnergy);
        return extractEnergy;
    }

    @Override
    public void setMaxReceive(int value) {
        maxReceive = value;
    }

    @Override
    public void setMaxExtract(int value) {
        maxExtract = value;
    }

    @Override
    public int getMaxReceive() {
        return maxReceive;
    }

    @Override
    public int getMaxExtract() {
        return maxExtract;
    }

    protected void internalEnergyChange(int changes) {
        if(changes != 0)
            onEnergyChanged(changes);
    }

    protected void onEnergyChanged(int changes) {

    }
}
