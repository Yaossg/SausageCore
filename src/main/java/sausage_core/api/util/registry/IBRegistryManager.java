package sausage_core.api.util.registry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.api.annotation.AutoCall;
import sausage_core.api.util.client.IBlockCM;
import sausage_core.api.util.client.IItemCM;
import sausage_core.api.util.client.IItemML;

import java.util.*;
import java.util.function.Function;

import static sausage_core.api.util.common.SausageUtils.nonnull;

public final class IBRegistryManager implements IRegistryManager {
	final String modid;
	public final CreativeTabs tab;
	final Map<Item, IItemML> items = new HashMap<>();
	final Map<Item, IItemCM> icms = new HashMap<>();
	final List<Block> blocks = new ArrayList<>();
	final Map<Block, IBlockCM> bcms = new HashMap<>();

	public IBRegistryManager(String modid) {
		this(modid, null);
		AutoCall.When.checkState();
	}

	public IBRegistryManager(String modid, CreativeTabs tab) {
		this.modid = modid;
		this.tab = tab;
	}

	public Item addItem(String name) {
		return addItem(name, new Item());
	}

	public <T extends Item> T addItem(String name, T item) {
		return addItem(name, item, IItemML::loadDefaultedModel);
	}

	public Item addItem(String name, IItemML iml) {
		return addItem(name, new Item(), iml);
	}

	public <T extends Item> T addItem(String name, T item, IItemML iml) {
		items.put(item.setTranslationKey(modid + "." + name).setRegistryName(name).setCreativeTab(tab), iml);
		return item;
	}

	public void addItemCM(Item item, IItemCM icm) {
		icms.put(item, icm);
	}

	public <T extends Block> T addOnlyBlock(String name, T block) {
		blocks.add(block.setTranslationKey(modid + "." + name).setRegistryName(name).setCreativeTab(tab));
		return block;
	}

	public <T extends Block> T addBlock(String name, T block) {
		return addBlock(name, block, ItemBlock::new);
	}

	public <T extends Block> T addBlock(String name, T block, Function<? super T, ItemBlock> itemBlockFactory) {
		return addBlock(name, block, itemBlockFactory, IItemML::loadDefaultedModel);
	}

	public <T extends Block> T addBlock(String name, T block, IItemML iml) {
		return addBlock(name, block, ItemBlock::new, iml);
	}

	public <T extends Block> T addBlock(String name, T block, Function<? super T, ItemBlock> itemBlockFactory, IItemML iml) {
		addOnlyBlock(name, block);
		items.put(itemBlockFactory.apply(block).setRegistryName(nonnull(block.getRegistryName())), iml);
		return block;
	}

	public void addBlockCM(Block block, IBlockCM bcm) {
		bcms.put(block, bcm);
	}

	// following 3 methods are going to be called automatically
	{
		R.add(this::register);
		LM.add(this::loadModel);
		LCM.add(this::loadCM);
	}
	@AutoCall(when = AutoCall.When.IB_REGISTER)
	private static final Set<Runnable> R = new HashSet<>();
	private void register() {
		blocks.forEach(ForgeRegistries.BLOCKS::register);
		items.keySet().forEach(ForgeRegistries.ITEMS::register);
	}

	@AutoCall(when = AutoCall.When.LOAD_MODEL, side = Side.CLIENT)
	private static final Set<Runnable> LM = new HashSet<>();
	private void loadModel() {
		items.forEach((item, iml) -> iml.loadModel(item));
	}

	@AutoCall(when = AutoCall.When.INIT, side = Side.CLIENT)
	private static final Set<Runnable> LCM = new HashSet<>();
	private void loadCM() {
		loadCMClient();
	}

	@SideOnly(Side.CLIENT)
	private void loadCMClient() {
		BlockColors bc = Minecraft.getMinecraft().getBlockColors();
		bcms.forEach((block, bcm) -> bc.registerBlockColorHandler(bcm::colorMultiplier, block));
		ItemColors ic = Minecraft.getMinecraft().getItemColors();
		icms.forEach((item, icm) -> ic.registerItemColorHandler(icm::colorMultiplier, item));
		bcms.forEach((block, bcm) -> ic.registerItemColorHandler(bcm::colorMultiplier, block));
	}

	@Override
	public String modid() {
		return modid;
	}

	@Override
	public Class<?>[] types() {
		return new Class[] {Item.class, Block.class};
	}
}
