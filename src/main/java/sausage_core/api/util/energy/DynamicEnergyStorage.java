package sausage_core.api.util.energy;

public abstract class DynamicEnergyStorage extends BasicEnergyStorage implements IEnergyDynamic {
    protected float efficiency;
    protected float receiveRatio;
    protected float extractRatio;
    public DynamicEnergyStorage(int capacity) {
        super(capacity);
    }

    public DynamicEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public DynamicEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public DynamicEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
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

    protected float getReceiveEfficiency() {
        return 1;
    }

    protected float getExtractEfficiency() {
        return 1;
    }

    @Override
    protected void onEnergyChanged(int changes) {
        if(getReceiveRatio() > 0)
            setMaxReceive((int) (getEnergyStored() * getReceiveRatio() * getReceiveEfficiency()));
        if(getExtractRatio() > 0)
            setMaxExtract((int) (getEnergyStored() * getExtractRatio() * getExtractEfficiency()));
    }
}
