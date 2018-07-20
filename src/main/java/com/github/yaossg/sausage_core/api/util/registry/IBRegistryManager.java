package com.github.yaossg.sausage_core.api.util.registry;

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

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public final class IBRegistryManager {
    public String modid;
    @Nullable
    public CreativeTabs tab;
    public final List<Item> items = NonNullList.create();
    public final List<Block> blocks = NonNullList.create();

    public IBRegistryManager(String modid) {
        this(modid, null);
    }

    public IBRegistryManager(String modid, @Nullable CreativeTabs tab) {
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
        return addBlock(block, name, ItemBlock::new);
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
        if(item.getHasSubtypes()) {
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
}
