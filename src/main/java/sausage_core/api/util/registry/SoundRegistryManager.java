package sausage_core.api.util.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SoundRegistryManager {
    public final String modid;
    final List<SoundEvent> sounds = new ArrayList<>();

    public SoundRegistryManager(String modid) {
        this.modid = modid;
    }

    public SoundEvent addSound(String name) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(modid, name));
        sounds.add(soundEvent.setRegistryName(soundEvent.getSoundName()));
        return soundEvent;
    }

    public void register() {
        sounds.forEach(ForgeRegistries.SOUND_EVENTS::register);
    }
}
