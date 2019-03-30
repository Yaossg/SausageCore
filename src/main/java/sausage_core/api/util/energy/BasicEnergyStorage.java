package sausage_core.api.util.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class BasicEnergyStorage extends EnergyStorage implements IEnergyModifiable, IEnergyView, INBTSerializable<NBTTagCompound> {
	public BasicEnergyStorage() {
		super(0);
	}

	@Override
	public void setEnergyStored(int value) {
		int old_energy = energy;
		energy = value;
		internalEnergyChanged(energy - old_energy);
	}

	@Override
	public void setMaxEnergyStored(int value) {
		capacity = value;
		int old_energy = energy;
		energy = Math.min(energy, capacity);
		internalEnergyChanged(energy - old_energy);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
		if(!simulate)
			internalEnergyChanged(receiveEnergy);
		return receiveEnergy;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int extractEnergy = super.extractEnergy(maxExtract, simulate);
		if(!simulate)
			internalEnergyChanged(-extractEnergy);
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

	protected void internalEnergyChanged(int changes) {
		if(changes != 0)
			onEnergyChanged(changes);
	}

	protected void onEnergyChanged(int changes) {

	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("capacity", capacity);
		nbt.setInteger("energy", energy);
		nbt.setInteger("maxReceive", maxReceive);
		nbt.setInteger("maxExtract", maxExtract);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		capacity = nbt.getInteger("capacity");
		energy = nbt.getInteger("energy");
		maxReceive = nbt.getInteger("maxReceive");
		maxExtract = nbt.getInteger("maxExtract");
	}
}
