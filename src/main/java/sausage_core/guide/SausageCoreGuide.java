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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sausage_core.SausageCore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@GuideBook
public class SausageCoreGuide implements IGuideBook {
	public static Book book;
	public static ItemStack stack;

	private void add(Map<ResourceLocation, EntryAbstract> entries, String category, String entry, ItemStack icon, int size) {
		List<IPage> pages = new ArrayList<>();
		String name = String.join(".", "guide", SausageCore.MODID, category, entry);
		for (int i = 0; i < size; ++i) {
			pages.add(new PageText(name + ".info." + i));
		}
		entries.put(new ResourceLocation(category, entry),
				new EntryItemStack(pages, name, icon));
	}

	@Nonnull
	@Override
	public Book buildBook() {
		ResourceLocation guide = new ResourceLocation(SausageCore.MODID, "guide");
		List<CategoryAbstract> categories = new ArrayList<>();
		Map<ResourceLocation, EntryAbstract> general = new LinkedHashMap<>();

		add(general, "general", "introduction", new ItemStack(SausageCore.sausage), 2);
		add(general, "general", "ingredient", new ItemStack(Blocks.CRAFTING_TABLE), 3);
		add(general, "general", "world_type", new ItemStack(Blocks.GRASS), 3);
		add(general, "general", "debugging", new ItemStack(SausageCore.debug_stick), 2);

		Map<ResourceLocation, EntryAbstract> chapter_2 = new LinkedHashMap<>();

		add(chapter_2, "chapter_2", "episode_1", new ItemStack(Items.PAPER), 4);

		categories.add(new CategoryItemStack(general, "guide.sausage_core.CATEGORY.general", new ItemStack(SausageCore.sausage)));
		categories.add(new CategoryItemStack(chapter_2, "guide.sausage_core.CATEGORY.chapter_2", new ItemStack(Items.WRITABLE_BOOK)));

		BookBinder binder = new BookBinder(guide);
		binder.setGuideTitle("guide.sausage_core.title");
		binder.setItemName("guide.sausage_core.title");
		binder.setCreativeTab(SausageCore.IB.tab);
		binder.setSpawnWithBook();
		categories.forEach(binder::addCategory);
		return book = binder.build();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleModel(ItemStack bookStack) {
		GuideAPI.setModel(book, new ResourceLocation(SausageCore.MODID, "guide"), "inventory");
	}
}
