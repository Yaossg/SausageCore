package com.github.yaossg.sausage_core.api.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

public class Utils {
    public static float lightLevelOf(int level) {
        return level / 16f;
    }

    public static void giveAdvancement(Entity player, String modid, String root, String advance) {
        if(player.getServer() != null && player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP)player;
            Advancement advancement = player.getServer().getAdvancementManager()
                    .getAdvancement(new ResourceLocation(modid, root + "/" + advance));
            AdvancementProgress progress = playerMP.getAdvancements().getProgress(advancement);
            if (!progress.isDone())
                for (String s : progress.getRemaningCriteria())
                    playerMP.getAdvancements().grantCriterion(advancement, s);
        }
    }

    /**
     * @param tileClass its name must begin with "Tile"
     * */
    public static void registerTile(Class<? extends TileEntity> tileClass, String modid)
    {
        GameRegistry.registerTileEntity(tileClass, modid + ":" + tileClass.getName().replaceFirst("Tile", ""));
    }

    public static void unstableWarning(Logger logger, String name, String version, String modid) {
        logger.info(name + " v" + version + " is loading (modid:" + modid + ")");
        logger.warn("The mod is still unstable and in early development, There are still lots of bugs remaining");
    }

}