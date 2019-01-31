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
        ResourceLocation soundName = new ResourceLocation(modid, name);
        SoundEvent soundEvent = new SoundEvent(soundName);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent.setRegistryName(soundName));
        return soundEvent;
    }
}
