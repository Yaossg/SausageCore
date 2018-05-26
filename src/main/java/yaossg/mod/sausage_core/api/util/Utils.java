package yaossg.mod.sausage_core.api.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

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
}