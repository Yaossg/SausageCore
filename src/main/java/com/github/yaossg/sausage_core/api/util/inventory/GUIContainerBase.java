package com.github.yaossg.sausage_core.api.util.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Default implementation of {@link GuiContainer} with a regular texture
 * */
public abstract class GUIContainerBase extends GuiContainer {
    private final ResourceLocation texture;

    public GUIContainerBase(Container inventorySlotsIn, int xSize, int ySize, ResourceLocation texture) {
        super(inventorySlotsIn);
        this.xSize = xSize;
        this.ySize = ySize;
        this.texture = texture;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f);

        mc.getTextureManager().bindTexture(texture);
        int offsetX = (width - xSize) / 2, offsetY = (height - ySize) / 2;

        drawTexturedModalRect(offsetX, offsetY, 0, 0, xSize, ySize);
    }
}
