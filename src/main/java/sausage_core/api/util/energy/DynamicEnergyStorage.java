package sausage_core.api.util.energy;

import net.minecraft.nbt.NBTTagCompound;

public abstract class DynamicEnergyStorage extends BasicEnergyStorage implements IEnergyDynamic {
    protected float efficiency;
    protected float receiveRatio;
    protected float extractRatio;
    protected int receiveUpperLimit;
    protected int extractLowerLimit;

    @Override
    public void setDefaults(int maxEnergyStored) {
        super.setDefaults(maxEnergyStored);
        setEfficiency(1);
        receiveUpperLimit = maxEnergyStored;
        extractLowerLimit = 1;
    }

    @Override
    public float getReceiveRatio() {
        return receiveRatio;
    }

    @Override
    public float getExtractRatio() {
        return extractRatio;
    }

    @Override
    public void setReceiveRatio(float value) {
        receiveRatio = value;
    }

    @Override
    public void setExtractRatio(float value) {
        extractRatio = value;
    }

    @Override
    public void setEfficiency(float value) {
        efficiency = value;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public int getReceiveUpperLimit() {
        return receiveUpperLimit;
    }

    @Override
    public int getExtractLowerLimit() {
        return extractLowerLimit;
    }

    @Override
    public void setReceiveUpperLimit(int value) {
        receiveUpperLimit = value;
    }

    @Override
    public void setExtractLowerLimit(int value) {
        extractLowerLimit = value;
    }

    protected float getReceiveEfficiency() {
        return 1;
    }

    protected float getExtractEfficiency() {
        return 1;
    }

    @Override
    protected void onEnergyChanged(int changes) {
        if(getReceiveRatio() > 0)
            setMaxReceive(Math.min(receiveUpperLimit, (int) ((getMaxEnergyStored() - getEnergyStored()) * getReceiveRatio() * getReceiveEfficiency())));
        if(getExtractRatio() > 0)
            setMaxExtract(Math.max(extractLowerLimit, (int) (getEnergyStored() * getExtractRatio() / getExtractEfficiency())));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        nbt.setFloat("efficiency", efficiency);
        nbt.setFloat("receiveRatio", receiveRatio);
        nbt.setFloat("extractRatio", extractRatio);
        nbt.setInteger("receiveUpperLimit", receiveUpperLimit);
        nbt.setInteger("extractLowerLimit", extractLowerLimit);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        efficiency = nbt.getFloat("efficiency");
        receiveRatio = nbt.getFloat("receiveRatio");
        extractRatio = nbt.getFloat("extractRatio");
        receiveUpperLimit = nbt.getInteger("receiveUpperLimit");
        extractLowerLimit = nbt.getInteger("extractLowerLimit");
    }
}
