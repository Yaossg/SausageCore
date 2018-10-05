package sausage_core.api.util.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundRegistryManager {
    public final String modid;

    public SoundRegistryManager(String modid) {
        this.modid = modid;
    }

    public SoundEvent addSound(String name) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(modid, name));
        ForgeRegistries.SOUND_EVENTS.register(soundEvent.setRegistryName(soundEvent.getSoundName()));
        return soundEvent;
    }
}
