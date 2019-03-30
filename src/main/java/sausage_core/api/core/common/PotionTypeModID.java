package sausage_core.api.core.common;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

public class PotionTypeModID extends PotionType {
	public PotionTypeModID(PotionEffect... potionEffect) {
		super(potionEffect);
	}

	public PotionTypeModID(String baseName, PotionEffect... potionEffect) {
		super(baseName, potionEffect);
	}

	private static final String EFFECT = "effect";

	@Override
	public String getNamePrefixed(String p_185174_1_) {
		String s = super.getNamePrefixed(p_185174_1_);
		int i = s.indexOf(EFFECT) + EFFECT.length();
		return s.substring(0, i + 1) + getRegistryName().getResourceDomain() + s.substring(i);
	}
}
