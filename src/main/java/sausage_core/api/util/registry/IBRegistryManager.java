package sausage_core.api.util.registry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.api.util.common.SausageUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class IBRegistryManager {
    public final String modid;
    @Nullable
    public final CreativeTabs tab;
    final List<Item> items = new ArrayList<>();
    final List<Block> blocks = new ArrayList<>();

    public IBRegistryManager(String modid) {
        this(modid, null);
    }

    public IBRegistryManager(String modid, @Nullable CreativeTabs tab) {
        this.modid = modid;
        this.tab = tab;
    }

    @Nonnull
    private CreativeTabs nonnull_tab() {
        return SausageUtils.nonnull(tab);
    }

    public Item addItem(String name) {
        return addItem(name, new Item());
    }

    @Deprecated
    public <T extends Item> T addItem(T item, String name) {
        return addItem(name, item);
    }

    public <T extends Item> T addItem(String name, T item) {
        items.add(item.setUnlocalizedName(modid + "." + name).setRegistryName(name).setCreativeTab(nonnull_tab()));
        return item;
    }

    @Deprecated
    public <T extends Block> T addBlock(T block, String name, Function<? super T, ItemBlock> itemBlockFactory) {
        return addBlock(name, block, itemBlockFactory);
    }

    public <T extends Block> T addBlock(String name, T block, Function<? super T, ItemBlock> itemBlockFactory) {
        blocks.add(block.setUnlocalizedName(modid + "." + name).setRegistryName(name).setCreativeTab(nonnull_tab()));
        items.add(itemBlockFactory.apply(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    @Deprecated
    public <T extends Block> T addBlock(T block, String name) {
        return addBlock(name, block);
    }


    public <T extends Block> T addBlock(String name, T block) {
        return addBlock(name, block, ItemBlock::new);
    }

    public void registerItems() {
        items.forEach(ForgeRegistries.ITEMS::register);
    }

    public void registerBlocks() {
        blocks.forEach(ForgeRegistries.BLOCKS::register);
    }

    public void registerAll() {
        registerItems();
        registerBlocks();
    }

    @SideOnly(Side.CLIENT)
    public void loadItemsModel(List<Item> exclude) {
        items.stream().filter(item -> !exclude.contains(item)).forEach(this::loadModel);
    }

    @SideOnly(Side.CLIENT)
    public void loadBlocksModel(List<Block> exclude) {
        blocks.stream().filter(block -> !exclude.contains(block)).forEach(this::loadModel);
    }

    @SideOnly(Side.CLIENT)
    public void loadItemsModel() {
        items.forEach(this::loadModel);
    }

    @SideOnly(Side.CLIENT)
    public void loadBlocksModel() {
        blocks.forEach(this::loadModel);
    }

    @SideOnly(Side.CLIENT)
    public void loadAllModel() {
        loadItemsModel();
        loadBlocksModel();
    }

    @SideOnly(Side.CLIENT)
    void loadModel(Item item) {
        loadModelDefault(new ItemStack(item));
    }

    @SideOnly(Side.CLIENT)
    void loadModel(Block block) {
        loadModelDefault(new ItemStack(block));
    }

    @SideOnly(Side.CLIENT)
    static void loadModelDefault(ItemStack stack) {
        ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(),
                new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory"));
    }
}
