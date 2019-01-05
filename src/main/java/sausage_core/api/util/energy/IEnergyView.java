package sausage_core.api.util.energy;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyView extends IEnergyStorage {
    default boolean isSupplier() {
        return true;
    }

    default boolean isConsumer() {
        return true;
    }

    default IEnergyView getAsSupplier() {
        return new IEnergyView() {
            private final IEnergyView storage = IEnergyView.this;

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

            @Override
            public boolean isConsumer() {
                return false;
            }
        };
    }

    default IEnergyView getAsConsumer() {
        return new IEnergyView() {
            private final IEnergyView storage = IEnergyView.this;

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

            @Override
            public boolean isSupplier() {
                return false;
            }
        };
    }
}
