package sausage_core.api.util.registry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.api.util.client.IBlockCM;
import sausage_core.api.util.client.IItemCM;
import sausage_core.api.util.common.SausageUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class IBRegistryManager {
	public final String modid;
	@Nullable
	public final CreativeTabs tab;
	protected final List<Item> items = new ArrayList<>();
	protected final Map<Item, IItemCM> icms = new HashMap<>();
	protected final List<Block> blocks = new ArrayList<>();
	protected final Map<Block, IBlockCM> bcms = new HashMap<>();

	public IBRegistryManager(String modid) {
		this(modid, null);
	}

	public IBRegistryManager(String modid, @Nullable CreativeTabs tab) {
		this.modid = modid;
		this.tab = tab;
	}

	@Nonnull
	public CreativeTabs nonnull_tab() {
		return SausageUtils.nonnull(tab);
	}

	public Item addItem(String name) {
		return addItem(name, new Item());
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	public <T extends Item> T addItem(T item, String name) {
		return addItem(name, item);
	}

	public <T extends Item> T addItem(String name, T item) {
		items.add(item.setUnlocalizedName(modid + "." + name).setRegistryName(name).setCreativeTab(nonnull_tab()));
		return item;
	}

	public void addItemCM(Item item, IItemCM icm) {
		icms.put(item, icm);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	public <T extends Block> T addBlock(T block, String name, Function<? super T, ItemBlock> itemBlockFactory) {
		return addBlock(name, block, itemBlockFactory);
	}

	public <T extends Block> T addBlock(String name, T block, Function<? super T, ItemBlock> itemBlockFactory) {
		blocks.add(block.setUnlocalizedName(modid + "." + name).setRegistryName(name).setCreativeTab(nonnull_tab()));
		items.add(itemBlockFactory.apply(block).setRegistryName(block.getRegistryName()));
		return block;
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	public <T extends Block> T addBlock(T block, String name) {
		return addBlock(name, block);
	}

	public <T extends Block> T addBlock(String name, T block) {
		return addBlock(name, block, ItemBlock::new);
	}

	public void addBlockCM(Block block, IBlockCM bcm) {
		bcms.put(block, bcm);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	public void registerItems() {
		items.forEach(ForgeRegistries.ITEMS::register);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	public void registerBlocks() {
		blocks.forEach(ForgeRegistries.BLOCKS::register);
	}

	public void registerAll() {
		blocks.forEach(ForgeRegistries.BLOCKS::register);
		items.forEach(ForgeRegistries.ITEMS::register);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void loadItemsModel(List<Item> exclude) {
		items.stream().filter(item -> !exclude.contains(item)).forEach(this::loadModel);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void loadBlocksModel(List<Block> exclude) {
		blocks.stream().filter(block -> !exclude.contains(block)).forEach(this::loadModel);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void loadItemsModel() {
		items.forEach(this::loadModel);
	}

	/**
	 * WARNING: for removal
	 * */
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void loadBlocksModel() {
		blocks.forEach(this::loadModel);
	}

	@SideOnly(Side.CLIENT)
	public void loadAllModel() {
		items.forEach(this::loadModel);
		blocks.forEach(this::loadModel);
	}

	@SideOnly(Side.CLIENT)
	protected void loadModel(Item item) {
		loadModel(new ItemStack(item));
	}

	@SideOnly(Side.CLIENT)
	protected void loadModel(Block block) {
		loadModel(new ItemStack(block));
	}

	@SideOnly(Side.CLIENT)
	protected void loadModel(ItemStack stack) {
		ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(),
				new ModelResourceLocation(SausageUtils.nonnull(stack.getItem().getRegistryName()), "inventory"));
	}

	private void checkStateCM() {
		if(!Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
			throw new IllegalStateException("It's too early to load item/block color multiplier");
	}

	@SideOnly(Side.CLIENT)
	protected void loadICMClient() {
		ItemColors ic = Minecraft.getMinecraft().getItemColors();
		icms.forEach((item, icm) -> ic.registerItemColorHandler(icm::colorMultiplier, item));
		bcms.forEach((block, bcm) -> ic.registerItemColorHandler(bcm::colorMultiplier, block));
	}

	@SideOnly(Side.CLIENT)
	protected void loadBCMClient() {
		BlockColors bc = Minecraft.getMinecraft().getBlockColors();
		bcms.forEach((block, bcm) -> bc.registerBlockColorHandler(bcm::colorMultiplier, block));
	}

	public void loadAllCM() {
		checkStateCM();
		switch (FMLCommonHandler.instance().getSide()) {
			case CLIENT:
				loadBCMClient();
				loadICMClient();
			case SERVER:
			default:
		}
	}
}
