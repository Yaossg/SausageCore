package sausage_core.api.core.atp;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class ATPHolder {
	List<IATPProvider> providers = new ArrayList<>();
	int amount;

	public void give(int value) {
		amount += value;
	}

	public boolean want(int value) {
		if(amount >= value) return true;
		for(IATPProvider provider : providers) {
			amount += provider.provide(value - amount);
			if(amount >= value) return true;
		}
		return false;
	}

	public int take(int value) {
		if(amount >= value) {
			amount -= value;
			return value;
		}
		return 0;
	}

	public int rest() {
		return take(amount);
	}

	public int amount() {
		return amount;
	}

	public void register(IATPProvider provider) {
		providers.add(Preconditions.checkNotNull(provider));
	}
}
