package com.github.yaossg.sausage_core.api.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

public class IBRegistryManager {
    public String modid;
    public CreativeTabs tab;
    private NonNullList<Item> items = NonNullList.create();
    private NonNullList<Block> blocks = NonNullList.create();
    public static final Function<Block, ItemBlock> defaultItemBlockFactory = ItemBlock::new;

    public IBRegistryManager(String modid, CreativeTabs tab) {
        this.modid = modid;
        this.tab = tab;
    }

    public Item addItem(Item item, String name) {
        items.add(item.setUnlocalizedName(modid + "." + name).setRegistryName(name).setCreativeTab(tab));
        return item;
    }

    public Block addBlock(Block block, String name, Function<Block, ItemBlock> itemBlockFactory) {
        blocks.add(block.setUnlocalizedName(modid + "." + name).setRegistryName(name).setCreativeTab(tab));
        items.add(itemBlockFactory.apply(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    public Block addBlock(Block block, String name) {
        return addBlock(block, name, defaultItemBlockFactory);
    }

    public void registerAllItems() {
        items.forEach(ForgeRegistries.ITEMS::register);
    }

    public void registerAllBlocks() {
        blocks.forEach(ForgeRegistries.BLOCKS::register);
    }

    public void registerAll() {
        registerAllItems();
        registerAllBlocks();
    }

    @SideOnly(Side.CLIENT)
    public void loadModelItems() {
        items.forEach(this::loadModel);
    }

    @SideOnly(Side.CLIENT)
    public void loadModelBlocks() {
        blocks.forEach(this::loadModel);
    }

    @SideOnly(Side.CLIENT)
    public void loadModelAll() {
        loadModelItems();
        loadModelBlocks();
    }

    @SideOnly(Side.CLIENT)
    void loadModel(Item item) {
        if (item.getHasSubtypes()) {
            NonNullList<ItemStack> items = NonNullList.create();
            item.getSubItems(tab, items);
            items.forEach(IBRegistryManager::loadModel);
        } else {
            loadModel(new ItemStack(item));
        }
    }

    @SideOnly(Side.CLIENT)
    void loadModel(Block block) {
        NonNullList<ItemStack> blocks = NonNullList.create();
        block.getSubBlocks(tab, blocks);
        blocks.forEach(IBRegistryManager::loadModel);
    }

    @SideOnly(Side.CLIENT)
    static void loadModel(ItemStack stack) {
        ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(),
                new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory"));
    }
    public static class FBRegistryManager extends IBRegistryManager {

        public FBRegistryManager(String modid) {
            super(modid, null);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void loadModelItems() {

        }

        @Override
        @SideOnly(Side.CLIENT)
        public void loadModelBlocks() {

        }
    }
}
