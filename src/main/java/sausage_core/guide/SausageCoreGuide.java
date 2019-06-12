package sausage_core.guide;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreIngredient;
import sausage_core.SausageCore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@GuideBook
public class SausageCoreGuide implements IGuideBook {

	public SausageCoreGuide() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static final String HAS = SausageCore.MODID + ".has_guide";

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		NBTTagCompound entityData = event.player.getEntityData();
		NBTTagCompound data = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if(!data.getBoolean(HAS)) {
			ItemHandlerHelper.giveItemToPlayer(event.player, stack);
			data.setBoolean(HAS, true);
			entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
	}

	public static Book book;
	public static ItemStack stack;

	private void add(Map<ResourceLocation, EntryAbstract> entries, String category, String entry, ItemStack icon, int... indexes) {
		List<IPage> pages = new ArrayList<>();
		String name = String.join(".", "guide", SausageCore.MODID, category, entry);
		if(indexes.length == 0)
			pages.add(new PageText(name + ".info"));
		else for(int index : indexes)
			pages.add(new PageText(name + ".info." + index));
		entries.put(new ResourceLocation(category, entry),
				new EntryItemStack(pages, name, icon));
	}

	@Nonnull
	@Override
	public Book buildBook() {
		ResourceLocation guide = new ResourceLocation(SausageCore.MODID, "guide");
		List<CategoryAbstract> categories = new ArrayList<>();
		Map<ResourceLocation, EntryAbstract> general = new LinkedHashMap<>();

		add(general, "general", "introduction", new ItemStack(SausageCore.sausage), 0, 1);
		add(general, "general", "ingredient", new ItemStack(Blocks.CRAFTING_TABLE), 0, 1, 2);
		add(general, "general", "world_type", new ItemStack(Blocks.GRASS), 0, 1, 2);
		add(general, "general", "debugging", new ItemStack(SausageCore.debug_stick), 0, 1);


		Map<ResourceLocation, EntryAbstract> second_chapter = new LinkedHashMap<>();
		ItemStack paper = new ItemStack(Items.PAPER);

		add(second_chapter, "second_chapter", "episode_1", paper, 1, 2);

		categories.add(new CategoryItemStack(general, "guide.sausage_core.CATEGORY.general", new ItemStack(SausageCore.sausage)));
		categories.add(new CategoryItemStack(second_chapter, "guide.sausage_core.CATEGORY.second_chapter", new ItemStack(Items.WRITABLE_BOOK)));
		BookBinder binder = new BookBinder(guide);
		binder.setGuideTitle("guide.sausage_core.title");
		binder.setItemName("guide.sausage_core.title");
		binder.setCreativeTab(SausageCore.manager.tab);
		categories.forEach(binder::addCategory);
		return book = binder.build();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleModel(ItemStack bookStack) {
		GuideAPI.setModel(book, new ResourceLocation(SausageCore.MODID, "info_card"), "inventory");
	}

	@Override
	public void handlePost(ItemStack bookStack) {
		stack = bookStack;
		GameRegistry.addShapedRecipe(new ResourceLocation(SausageCore.MODID, "sausage"), null,
				new ItemStack(SausageCore.sausage), "mrm", "ror", "mrm",'m', Items.PORKCHOP, 'r', Items.SUGAR, 'o', Items.GUNPOWDER);
		GameRegistry.addShapedRecipe(new ResourceLocation(SausageCore.MODID, "guide"), null,
				stack, " x ","wow"," x ", 'x', Items.PAPER, 'o', SausageCore.sausage, 'w', new OreIngredient("dyeOrange"));
	}
}
