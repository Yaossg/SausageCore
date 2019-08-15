package sausage_core.api.util.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.api.util.common.SausageUtils;

@SideOnly(Side.CLIENT)
public final class GUIHelper {
	private GUIHelper() {}
	public static ResourceLocation getTexture(String modid, String... path) {
		return new ResourceLocation(modid, "textures/gui/" + String.join("/", path) + ".png");
	}

	public static ResourceLocation getTexture(String modid, String texture) {
		return getTexture(modid, "container", texture);
	}

	public static <T extends Container> T getContainer(GuiContainer guiContainer) {
		return SausageUtils.rawtype(guiContainer.inventorySlots);
	}

	public static void drawCenteredString(GuiContainer guiContainer, FontRenderer renderer, String string, int y, int color, boolean shadow) {
		if (shadow)
			guiContainer.drawCenteredString(renderer, string, guiContainer.getXSize() / 2, y, color);
		else
			renderer.drawString(string, (guiContainer.getXSize() - renderer.getStringWidth(string)) / 2, y, color);
	}

	public static void drawCenteredString(GuiContainer guiContainer, FontRenderer renderer, String string, int y, int color) {
		drawCenteredString(guiContainer, renderer, string, y, color, false);
	}
}
