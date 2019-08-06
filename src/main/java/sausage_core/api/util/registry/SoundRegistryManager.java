package sausage_core.api.util.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class SoundRegistryManager implements IRegistryManager {
	final String modid;

	public SoundRegistryManager(String modid) {
		this.modid = modid;
	}

	public SoundEvent register(String name) {
		ResourceLocation soundName = new ResourceLocation(modid, name);
		SoundEvent soundEvent = new SoundEvent(soundName);
		ForgeRegistries.SOUND_EVENTS.register(soundEvent.setRegistryName(soundName));
		return soundEvent;
	}

	@Override
	public String modid() {
		return modid;
	}

	@Override
	public Class<?>[] types() {
		return new Class[] {SoundEvent.class};
	}
}
