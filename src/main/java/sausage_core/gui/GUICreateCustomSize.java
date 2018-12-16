package sausage_core.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import sausage_core.util.WorldTypeUtils;

@SideOnly(Side.CLIENT)
public class GUICreateCustomSize extends GuiScreen {
    private final GuiCreateWorld createWorldGui;
    private int size;
    private GuiTextField field;
    private String title;

    public GUICreateCustomSize(GuiCreateWorld createWorldGuiIn) {
        createWorldGui = createWorldGuiIn;
        size = WorldTypeUtils.parseInt(createWorldGuiIn.chunkProviderSettingsJson);
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        title = I18n.format("generator.sausage_core.customSize.title");
        field = new GuiTextField(10, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        field.setText(String.valueOf(size));
        field.setFocused(true);
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
    }
    
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                createWorldGui.chunkProviderSettingsJson = field.getText();
                mc.displayGuiScreen(createWorldGui);
                break;
            case 1:
                mc.displayGuiScreen(createWorldGui);
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        field.textboxKeyTyped(typedChar, keyCode);
        size = WorldTypeUtils.parseInt(field.getText());
        if (keyCode == 28 || keyCode == 156)
            actionPerformed(this.buttonList.get(0));
    }

    @Override
    public void updateScreen() {
        field.updateCursorCounter();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, title, width / 2, 8, 16777215);
        field.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}