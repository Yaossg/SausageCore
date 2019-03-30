package sausage_core.api.util.energy;

public interface IEnergyDynamic extends IEnergyEfficiency {
	float getReceiveRatio();

	float getExtractRatio();

	void setReceiveRatio(float value);

	void setExtractRatio(float value);

	int getReceiveUpperLimit();

	int getExtractLowerLimit();

	void setReceiveUpperLimit(int value);

	void setExtractLowerLimit(int value);
}
