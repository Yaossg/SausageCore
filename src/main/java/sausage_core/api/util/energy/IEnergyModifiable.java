package sausage_core.api.util.energy;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyModifiable extends IEnergyStorage {
	void setEnergyStored(int value);

	void setMaxEnergyStored(int value);

	void setMaxReceive(int value);

	void setMaxExtract(int value);

	int getMaxReceive();

	int getMaxExtract();

	default void setMaxTransfer(int value) {
		setMaxReceive(value);
		setMaxExtract(value);
	}

	default void clear() {
		setEnergyStored(0);
	}

	default void fill() {
		setEnergyStored(getMaxEnergyStored());
	}

	default boolean isEmpty() {
		return getEnergyStored() == 0;
	}

	default void setDefaults(int maxEnergyStored) {
		setMaxEnergyStored(maxEnergyStored);
	}
}
