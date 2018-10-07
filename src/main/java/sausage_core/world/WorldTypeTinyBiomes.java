package sausage_core.world;

import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldTypeTinyBiomes extends WorldType {

    public WorldTypeTinyBiomes() {
        super("tinyBiomes");
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }

    @Override
    public String getTranslationKey() {
        return "generator.sausage_core.tinyBiomes";
    }

    @SubscribeEvent
    public void onGenLayer(WorldTypeEvent.BiomeSize event) {
        if(event.getWorldType() == this) event.setNewSize(2);
    }
}
