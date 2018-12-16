package sausage_core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUICreateCrazyBiomes extends GuiScreen {
    private final GuiCreateWorld createWorldGui;
    private String title;

    public GUICreateCrazyBiomes(GuiCreateWorld createWorldGuiIn) {
        createWorldGui = createWorldGuiIn;
    }
    
    public void initGui() {
        title = I18n.format("generator.sausage_core.buffet.title");
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
    }
    
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(createWorldGui);
                break;
            case 1:
                mc.displayGuiScreen(createWorldGui);
                break;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, title, width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}