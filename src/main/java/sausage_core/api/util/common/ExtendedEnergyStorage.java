package sausage_core.api.util.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * extended energy storage
 * has a basement storage and an addition storage to simplify energy upgrade
 * implements INBTSerializable to simplify NBT I/O
 * can set maxReceive & maxExtract after construction
 * can build view of supplier or consumer
 * */
public class ExtendedEnergyStorage extends EnergyStorage implements INBTSerializable<NBTTagCompound> {
    protected int basement;
    protected int addition;

    public ExtendedEnergyStorage(int capacity, int maxTransfer) {
        super(capacity);
        basement = capacity;
        addition = 0;
        setMaxTransfer(maxTransfer);
        refresh();
    }

    protected void refresh() {
        capacity = basement + addition;
        energy = Math.min(capacity, energy);
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setMaxTransfer(int maxTransfer) {
        setMaxReceive(maxTransfer);
        setMaxExtract(maxTransfer);
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int setEnergyStored(int energy) {
        this.energy = energy;
        refresh();
        return this.energy;
    }

    @Deprecated
    public int forceReceiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, maxReceive);
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Deprecated
    public int forceExtractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Deprecated
    public void setBasement(int basement) {
        this.basement = basement;
        refresh();
    }

    public void setAddition(int addition) {
        this.addition = addition;
        refresh();
    }

    public int getBasement() {
        return basement;
    }

    public int getAddition() {
        return addition;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("energy", energy);
        nbt.setInteger("basement", basement);
        nbt.setInteger("addition", addition);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energy = nbt.getInteger("energy");
        basement = nbt.getInteger("basement");
        addition = nbt.getInteger("addition");
        refresh();
    }

    protected IEnergyStorage supplier = null, consumer = null;

    public <T> T supplier() {
        if(supplier == null) {
            supplier = new IEnergyStorage() {
                private final ExtendedEnergyStorage storage = ExtendedEnergyStorage.this;

                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return 0;
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return storage.extractEnergy(maxExtract, simulate);
                }

                @Override
                public int getEnergyStored() {
                    return storage.getEnergyStored();
                }

                @Override
                public int getMaxEnergyStored() {
                    return storage.getMaxEnergyStored();
                }

                @Override
                public boolean canExtract() {
                    return storage.canExtract();
                }

                @Override
                public boolean canReceive() {
                    return false;
                }
            };
        }
        return SausageUtils.rawtype(supplier);
    }


    public <T> T consumer() {
        if(consumer == null) {
            consumer = new IEnergyStorage() {
                private final ExtendedEnergyStorage storage = ExtendedEnergyStorage.this;

                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return storage.receiveEnergy(maxReceive, simulate);
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return 0;
                }

                @Override
                public int getEnergyStored() {
                    return storage.getEnergyStored();
                }

                @Override
                public int getMaxEnergyStored() {
                    return storage.getMaxEnergyStored();
                }

                @Override
                public boolean canExtract() {
                    return false;
                }

                @Override
                public boolean canReceive() {
                    return storage.canReceive();
                }
            };
        }
        return SausageUtils.rawtype(consumer);
    }
}
