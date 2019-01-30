package sausage_core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.SausageCore;
import sausage_core.world.WorldTypeBuffet;

@SideOnly(Side.CLIENT)
public class GUICreateBuffetWorld extends GuiScreen {
    private final GuiCreateWorld createWorldGui;
    private int biome;
    private String title;
    private GuiButton text;
    public ResourceLocation getBiomeID() {
        return WorldTypeBuffet.BIOMES.get(biome).getRegistryName();
    }

    public String getDisplayBiome() {
        return String.format("[Biome ID: %d] %s", biome, getBiomeID());
    }

    public GUICreateBuffetWorld(GuiCreateWorld createWorldGuiIn) {
        createWorldGui = createWorldGuiIn;
        biome = SausageCore.parseInt(createWorldGuiIn.chunkProviderSettingsJson);
    }
    
    public void initGui() {
        title = I18n.format("generator.sausage_core.buffet.title");
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
        buttonList.add(text = new GuiButton(10, width / 2 - 100, height / 2 - 20, 200, 20, getDisplayBiome()));
        buttonList.add(new GuiButton(2, width / 2 - 125, height / 2 - 20, 20, 20, "<"));
        buttonList.add(new GuiButton(3, width / 2 + 105, height / 2 - 20, 20, 20, ">"));
    }
    
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                createWorldGui.chunkProviderSettingsJson = String.valueOf(biome);
                mc.displayGuiScreen(createWorldGui);
                break;
            case 1:
                mc.displayGuiScreen(createWorldGui);
                break;
            case 2:
                prevBiome();
                break;
            case 3:
                nextBiome();
                break;
            case 10:
                nextModID();
        }
        text.displayString = getDisplayBiome();
    }

    private void nextModID() {
        String modid = getBiomeID().getResourceDomain();
        for (int i = biome + 1; i < WorldTypeBuffet.BIOMES.size(); ++i) {
            if(!WorldTypeBuffet.BIOMES.get(i).getRegistryName().getResourceDomain().equals(modid)) {
                biome = i;
                return;
            }
        }
        for (int i = 0; i < biome; ++i) {
            if(!WorldTypeBuffet.BIOMES.get(i).getRegistryName().getResourceDomain().equals(modid)) {
                biome = i;
                return;
            }
        }
    }

    private void nextBiome() {
        if(++biome >= WorldTypeBuffet.BIOMES.size())
            biome = 0;
    }

    private void prevBiome() {
        if(--biome < 0)
            biome = WorldTypeBuffet.BIOMES.size() - 1;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, title, width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}