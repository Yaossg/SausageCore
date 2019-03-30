package sausage_core.api.core.common;

import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldTypeModID extends WorldType {
	private final String modid;

	public WorldTypeModID(String modid, String name) {
		super(name);
		this.modid = modid;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslationKey() {
		return String.join(".", "generator", modid, getName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getInfoTranslationKey() {
		return getTranslationKey() + ".info";
	}
}
