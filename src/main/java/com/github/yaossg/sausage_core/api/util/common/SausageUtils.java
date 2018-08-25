package com.github.yaossg.sausage_core.api.util.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SausageUtils {
    /**
     * used for {@link net.minecraft.block.Block#setLightLevel(float)}
     * convert integer light level to float one.
     * */
    public static float lightLevelOf(int level) {
        return level / 16f;
    }

    /**
     * give an advancement to player
     * all of pre-advancements will be given at the same time
     * @param player who will be given.
     */
    public static void giveAdvancement(Entity player, String modid, String root, String advance) {
        if(player.getServer() != null && player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            Advancement advancement = player.getServer().getAdvancementManager()
                    .getAdvancement(new ResourceLocation(modid, root + "/" + advance));
            checkNotNull(advancement, "unable to find such an advancement: %s:%s/%s", modid, root, advance);
            AdvancementProgress progress = playerMP.getAdvancements().getProgress(advancement);
            if(!progress.isDone())
                for (String s : progress.getRemaningCriteria())
                    playerMP.getAdvancements().grantCriterion(advancement, s);
        }
    }

    /**
     * @param tileClass is a class whose name starts with "Tile"qa
     */
    public static void registerTile(Class<? extends TileEntity> tileClass, String modid) {
        GameRegistry.registerTileEntity(tileClass, modid + ":" + tileClass.getSimpleName().replaceFirst("Tile", ""));
    }

    /**
     * used for Yaossg's mod including SausageCore itself
     * DO NOT use this if you don't like sausage
     * */
    public static void unstableWarning(Logger logger, String name, String version, String modid) {
        logger.info("{} (modid:{}) v{} is loading now", name, modid, version);
        logger.warn("The mod is still unstable and in early development and full of bugs");
        logger.warn("If you find any bug, please create a new issue on github.com/Yaossg/{} ", name);
    }

}