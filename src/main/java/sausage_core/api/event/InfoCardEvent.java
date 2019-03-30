package sausage_core.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.EventBus;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * This event is fired when a player uses a info card.
 * <br>
 * This event is not {@link Cancelable}.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link InfoCardEvent#INFO_CARD_BUS}.
 */
public class InfoCardEvent extends PlayerEvent {
	public static final EventBus INFO_CARD_BUS = new EventBus();

	public static void fire(EntityPlayer player) {
		InfoCardEvent event = new InfoCardEvent(player);
		INFO_CARD_BUS.post(event);
		player.sendMessage(event.modInfo.build());
	}

	public static class ModInfo {
		private final List<ITextComponent> components = NonNullList.create();
		private Style current_style = new Style();
		private String current_modid = "";

		public ModInfo addModTitle(String modid, String name, String version, String author) {
			current_modid = modid;
			return withStyle()
					.addText("========================================\n") // 40 =s
					.addTextF(style -> style.setBold(true), "%s %s - By %s", name, version, author)
					.newline();
		}

		public ModInfo newline() {
			return withStyle().addText("\n");
		}

		public ModInfo withStyle() {
			return withStyle(style -> new Style());
		}

		public ModInfo withStyle(UnaryOperator<Style> style) {
			current_style = style.apply(current_style);
			return this;
		}

		public ModInfo addText(String s) {
			return addText(UnaryOperator.identity(), s);
		}

		public ModInfo addText(UnaryOperator<Style> style, String s) {
			components.add(new TextComponentString(s).setStyle(style.apply(current_style.createDeepCopy())));
			return this;
		}

		public ModInfo addTextF(String s, Object... args) {
			return addTextF(UnaryOperator.identity(), s, args);
		}

		public ModInfo addTextF(UnaryOperator<Style> style, String s, Object... args) {
			return addText(style, String.format(s, args));
		}

		public ModInfo addURL(String url) {
			return addURL(url, url);
		}

		public ModInfo addURL(String s, String url) {
			addText(style -> style.setUnderlined(true)
					.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
					.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new TextComponentString(url))), s
			);
			return this;
		}

		public ModInfo addI18nText(String s, Object... args) {
			return addI18nText(UnaryOperator.identity(), s, args);
		}

		public ModInfo addI18nText(UnaryOperator<Style> style, String s, Object... args) {
			components.add(new TextComponentTranslation(String.join(".", current_modid, "info_card", s), args)
					.setStyle(style.apply(current_style.createDeepCopy())));
			return this;
		}

		public ITextComponent build() {
			TextComponentString ret = new TextComponentString("");
			for(ITextComponent component : components) ret.appendSibling(component);
			return ret;
		}
	}

	public final ModInfo modInfo = new ModInfo();

	private InfoCardEvent(EntityPlayer player) {
		super(player);
	}
}
