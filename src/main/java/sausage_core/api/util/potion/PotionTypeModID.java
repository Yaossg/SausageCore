package sausage_core.api.util.potion;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

public class PotionTypeModID extends PotionType {

    public PotionTypeModID(PotionEffect... potionEffect) {
        super(potionEffect);
    }

    public PotionTypeModID(String baseName, PotionEffect... potionEffect) {
        super(baseName, potionEffect);
    }

    @Override
    public String getNamePrefixed(String p_185174_1_) {
        return super.getNamePrefixed(p_185174_1_).replace("effect.", "effect." + getRegistryName().getResourceDomain() + ".");
    }
}
