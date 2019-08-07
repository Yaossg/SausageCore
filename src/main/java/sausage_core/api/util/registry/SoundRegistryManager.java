package sausage_core.api.util.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class SoundRegistryManager extends RegistryManagerBase {
	public SoundRegistryManager(String modid) {
		super(modid, false);
	}

	public SoundEvent register(String name) {
		ResourceLocation soundName = newIdentifier(name);
		SoundEvent soundEvent = new SoundEvent(soundName);
		ForgeRegistries.SOUND_EVENTS.register(soundEvent.setRegistryName(soundName));
		return soundEvent;
	}
}
