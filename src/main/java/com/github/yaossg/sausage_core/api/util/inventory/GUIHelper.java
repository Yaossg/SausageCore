package com.github.yaossg.sausage_core.api.util.inventory;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GUIHelper {
    @SideOnly(Side.CLIENT)
    public static ResourceLocation getTexture(String modid, String root, String texture) {
        return new ResourceLocation(modid, "textures/gui/" + root + "/" + texture + ".png");
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getTexture(String modid, String texture) {
        return getTexture(modid, "container", texture);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public static <T extends Container> T getContainer(GuiContainer guiContainer) {
        return (T) guiContainer.inventorySlots;
    }

    @SideOnly(Side.CLIENT)
    public static void drawCenteredString(GuiContainer guiContainer, FontRenderer renderer, String string, int y, int color, boolean shadow) {
        if(shadow)
            guiContainer.drawCenteredString(renderer, string, guiContainer.getXSize() / 2, y, color);
        else
            renderer.drawString(string, (guiContainer.getXSize() - renderer.getStringWidth(string)) / 2, y, color);
    }

    @SideOnly(Side.CLIENT)
    public static void drawCenteredString(GuiContainer guiContainer, FontRenderer renderer, String string, int y, int color) {
        drawCenteredString(guiContainer, renderer, string, y, color, false);
    }


}
