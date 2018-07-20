package com.github.yaossg.sausage_core.api.util.inventory;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GUIHelper {
    public static ResourceLocation getTexture(String modid, String texture) {
        return new ResourceLocation(modid, "textures/gui/container/" + texture + ".png");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Container> T getContainer(GuiContainer guiContainer) {
        return (T) guiContainer.inventorySlots;
    }

    public static void drawCenteredString(GuiContainer guiContainer, FontRenderer renderer, String string, int y, int color, boolean shadow) {
        if(shadow)
            guiContainer.drawCenteredString(renderer, string, guiContainer.getXSize() / 2, y, color);
        else
            renderer.drawString(string, (guiContainer.getXSize() - renderer.getStringWidth(string)) / 2, y, color);
    }

    public static void drawCenteredString(GuiContainer guiContainer, FontRenderer renderer, String string, int y, int color) {
        drawCenteredString(guiContainer, renderer, string, y, color, false);
    }


}
