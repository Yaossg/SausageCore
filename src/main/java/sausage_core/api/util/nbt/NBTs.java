package sausage_core.api.util.nbt;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import sausage_core.api.util.common.SausageUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.*;

import static net.minecraftforge.common.util.Constants.NBT.*;

public final class NBTs {

	// following methods convert raw data to NBTs

	public static NBTTagByte of(byte arg) {
		return new NBTTagByte(arg);
	}

	public static NBTTagShort of(short arg) {
		return new NBTTagShort(arg);
	}

	public static NBTTagInt of(int arg) {
		return new NBTTagInt(arg);
	}

	public static NBTTagLong of(long arg) {
		return new NBTTagLong(arg);
	}

	public static NBTTagFloat of(float arg) {
		return new NBTTagFloat(arg);
	}

	public static NBTTagDouble of(double arg) {
		return new NBTTagDouble(arg);
	}

	public static NBTTagString of(char arg) {
		return of(String.valueOf(arg));
	}

	public static NBTTagByte of(boolean arg) {
		return new NBTTagByte((byte) (arg ? 1 : 0));
	}

	public static NBTTagString of(String arg) {
		return new NBTTagString(arg);
	}

	public static NBTTagByteArray of(byte[] arg) {
		return new NBTTagByteArray(arg);
	}

	public static NBTTagIntArray of(int[] arg) {
		return new NBTTagIntArray(arg);
	}

	public static NBTTagLongArray of(long[] arg) {
		return new NBTTagLongArray(arg);
	}

	public static NBTTagByteArray of(ByteList arg) {
		return new NBTTagByteArray(arg);
	}

	public static NBTTagIntArray of(IntList arg) {
		return new NBTTagIntArray(arg);
	}

	public static NBTTagLongArray of(LongList arg) {
		return new NBTTagLongArray(arg);
	}

	public static NBTTagByteArray arrayOf(byte... arg) {
		return of(arg);
	}

	public static NBTTagIntArray arrayOf(int... arg) {
		return of(arg);
	}

	public static NBTTagLongArray arrayOf(long... arg) {
		return of(arg);
	}

	public static NBTTagList of(Iterable<NBTBase> arg) {
		NBTTagList list = new NBTTagList();
		for(NBTBase each : arg)
			list.appendTag(each);
		return list;
	}

	public static NBTTagList stringListOf(Iterable<String> arg) {
		NBTTagList list = new NBTTagList();
		for(String each : arg)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagCompound of(String k1, NBTBase v1) {
		return of(ImmutableMap.of(k1, v1));
	}

	public static NBTTagCompound of(String k1, NBTBase v1, String k2, NBTBase v2) {
		return of(ImmutableMap.of(k1, v1, k2, v2));
	}

	public static NBTTagCompound of(String k1, NBTBase v1, String k2, NBTBase v2, String k3, NBTBase v3) {
		return of(ImmutableMap.of(k1, v1, k2, v2, k3, v3));
	}

	public static NBTTagCompound of(String k1, NBTBase v1, String k2, NBTBase v2, String k3, NBTBase v3, String k4, NBTBase v4) {
		return of(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4));
	}

	public static NBTTagCompound of(String k1, NBTBase v1, String k2, NBTBase v2, String k3, NBTBase v3, String k4, NBTBase v4, String k5, NBTBase v5) {
		return of(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
	}

	public static NBTTagCompound of(Map<String, NBTBase> arg) {
		NBTTagCompound map = new NBTTagCompound();
		for(Map.Entry<String, NBTBase> entry : arg.entrySet())
			map.setTag(entry.getKey(), entry.getValue());
		return map;
	}

	public static NBTTagCompound stringMapOf(Map<String, String> arg) {
		NBTTagCompound map = new NBTTagCompound();
		for(Map.Entry<String, String> entry : arg.entrySet())
			map.setTag(entry.getKey(), of(entry.getValue()));
		return map;
	}

	public static NBTTagList asList(byte... args) {
		NBTTagList list = new NBTTagList();
		for(byte each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(short... args) {
		NBTTagList list = new NBTTagList();
		for(short each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(int... args) {
		NBTTagList list = new NBTTagList();
		for(int each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(long... args) {
		NBTTagList list = new NBTTagList();
		for(long each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(float... args) {
		NBTTagList list = new NBTTagList();
		for(float each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(double... args) {
		NBTTagList list = new NBTTagList();
		for(double each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(String... args) {
		NBTTagList list = new NBTTagList();
		for(String each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(byte[]... args) {
		NBTTagList list = new NBTTagList();
		for(byte[] each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(int[]... args) {
		NBTTagList list = new NBTTagList();
		for(int[] each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(long[]... args) {
		NBTTagList list = new NBTTagList();
		for(long[] each : args)
			list.appendTag(of(each));
		return list;
	}

	public static NBTTagList asList(NBTBase... args) {
		NBTTagList list = new NBTTagList();
		for(NBTBase each : args)
			list.appendTag(each);
		return list;
	}

	@SafeVarargs
	public static <T> NBTTagList asList(Function<T, ? extends NBTBase> function, T... args) {
		NBTTagList list = new NBTTagList();
		for(T each : args)
			list.appendTag(function.apply(each));
		return list;
	}

	// following methods convert NBTs to raw data

	public static byte raw(NBTTagByte arg) {
		return arg.getByte();
	}

	public static short raw(NBTTagShort arg) {
		return arg.getShort();
	}

	public static int raw(NBTTagInt arg) {
		return arg.getInt();
	}

	public static long raw(NBTTagLong arg) {
		return arg.getLong();
	}

	public static float raw(NBTTagFloat arg) {
		return arg.getFloat();
	}

	public static double raw(NBTTagDouble arg) {
		return arg.getDouble();
	}

	public static String raw(NBTTagString arg) {
		return arg.getString();
	}

	public static byte[] raw(NBTTagByteArray arg) {
		return arg.getByteArray();
	}

	public static int[] raw(NBTTagIntArray arg) {
		return arg.getIntArray();
	}

	public static long[] raw(NBTTagLongArray arg) {
		return arg.data; //AT
	}

	public static Stream<NBTBase> stream(NBTTagList list) {
		return Streams.stream(list);
	}

	public static List<NBTBase> list(NBTTagList list) {
		return stream(list).collect(Collectors.toList());
	}

	public static Set<String> keySet(NBTTagCompound compound) {
		return compound.getKeySet();
	}

	public static Stream<String> keys(NBTTagCompound compound) {
		return keySet(compound).stream();
	}

	public static Map<String, NBTBase> map(NBTTagCompound compound) {
		return keys(compound).collect(Collectors.toMap(Function.identity(), compound::getTag));
	}

	public static Stream<NBTBase> values(NBTTagCompound compound) {
		return keys(compound).map(compound::getTag);
	}

	public static List<NBTBase> valueList(NBTTagCompound compound) {
		return values(compound).collect(Collectors.toList());
	}

	public static Set<Map.Entry<String, NBTBase>> entrySet(NBTTagCompound compound) {
		return map(compound).entrySet();
	}

	public static Stream<Map.Entry<String, NBTBase>> entries(NBTTagCompound compound) {
		return entrySet(compound).stream();
	}

	public static IntStream stream(NBTTagByteArray array) {
		IntStream.Builder builder = IntStream.builder();
		for(byte b : raw(array)) builder.add(b);
		return builder.build();
	}

	public static IntStream stream(NBTTagIntArray array) {
		return Arrays.stream(raw(array));
	}

	public static LongStream stream(NBTTagLongArray array) {
		return Arrays.stream(raw(array));
	}

	// Collectors

	public static Collector<? extends NBTBase, ?, NBTTagList> toNBTList() {
		return Collector.of(NBTTagList::new, NBTTagList::appendTag, (r1, r2) -> {
			r2.forEach(r1::appendTag);
			return r1;
		});
	}

	public static <T> Collector<T, ?, NBTTagCompound>
	toNBTCompound(Function<? super T, String> keyMapper, Function<? super T, ? extends NBTBase> valueMapper) {
		return Collector.of(NBTTagCompound::new, (map, element) -> map.merge(NBTs.of(keyMapper.apply(element),
				valueMapper.apply(element))), (r1, r2) -> {
			r1.merge(r2);
			return r1;
		});
	}

	// following methods with 'OrCreate' never cause NPE

	public static NBTTagCompound getOrCreateTag(ItemStack stack) {
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		return SausageUtils.nonnull(stack.getTagCompound());
	}

	public static void setOrCreateSubTag(ItemStack stack, String name, NBTBase nbt) {
		getOrCreateTag(stack).setTag(name, nbt);
	}

	public static <T extends NBTBase> T getOrCreateSubTag(ItemStack stack, String name, T def) {
		NBTTagCompound tag = getOrCreateTag(stack);
		if(!tag.hasKey(name)) tag.setTag(name, def);
		return SausageUtils.rawtype(tag.getTag(name));
	}

	// following methods are for NBTs' highlight

	private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

	private static String handleEscape(String p_193582_0_) {
		return SIMPLE_VALUE.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.quoteAndEscape(p_193582_0_);
	}

	private static final Style DIGITS_STYLE = new Style().setColor(TextFormatting.GOLD);
	private static final Style LITERAL_STYLE = new Style().setColor(TextFormatting.RED);
	private static final Style STRING_STYLE = new Style().setColor(TextFormatting.GREEN);
	private static final Style KEY_STYLE = new Style().setColor(TextFormatting.AQUA);

	public static ITextComponent highlight(NBTBase nbt) {
		if(nbt instanceof NBTPrimitive) return highlight((NBTPrimitive) nbt);
		ITextComponent ret = new TextComponentString("");
		switch(nbt.getId()) {
			case TAG_STRING:
				ret.appendText("\"");
				String raw = nbt.toString();
				ret.appendSibling(new TextComponentString(raw.substring(1, raw.length() - 1)).setStyle(STRING_STYLE));
				ret.appendText("\"");
				break;
			case TAG_LIST:
				ret.appendText("[");
				NBTTagList list = (NBTTagList) nbt;
				for(int i = 0; i < list.tagCount(); ++i) {
					if(i != 0) ret.appendText(", ");
					ret.appendSibling(highlight(list.get(i)));
				}
				ret.appendText("]");
				break;
			case TAG_COMPOUND:
				NBTTagCompound compound = (NBTTagCompound) nbt;
				Collection<String> collection = compound.getKeySet();
				ret.appendText("{");
				boolean first = true;
				for(String s : collection) {
					if(!first) ret.appendText(", ");
					first = false;
					ret.appendSibling(new TextComponentString(handleEscape(s)).setStyle(KEY_STYLE));
					ret.appendText(": ");
					ret.appendSibling(highlight(compound.getTag(s)));
				}
				ret.appendText("}");
				break;
			case TAG_BYTE_ARRAY:
			case TAG_INT_ARRAY:
			case TAG_LONG_ARRAY:
				ret.appendSibling(handleArrays(nbt));
		}
		return ret;
	}

	private static ITextComponent handleArrays(NBTBase nbt) {
		ITextComponent ret = new TextComponentString("[");
		String type = "";
		switch(nbt.getId()) {
			case TAG_BYTE_ARRAY:
				type = "B";
				break;
			case TAG_INT_ARRAY:
				type = "I";
				break;
			case TAG_LONG_ARRAY:
				type = "L";
				break;
		}
		ret.appendSibling(new TextComponentString(type).setStyle(LITERAL_STYLE));
		ret.appendText("; ");
		switch(nbt.getId()) {
			case TAG_BYTE_ARRAY: {
				byte[] raw = raw((NBTTagByteArray) nbt);
				for(int i = 0; i < raw.length; ++i) {
					if(i != 0) ret.appendText(", ");
					ret.appendSibling(new TextComponentString(String.valueOf(raw[i])).setStyle(DIGITS_STYLE))
							.appendSibling(new TextComponentString(type).setStyle(LITERAL_STYLE));
				}
			}
			break;
			case TAG_INT_ARRAY: {
				int[] raw = raw((NBTTagIntArray) nbt);
				for(int i = 0; i < raw.length; ++i) {
					if(i != 0) ret.appendText(", ");
					ret.appendSibling(new TextComponentString(String.valueOf(raw[i])).setStyle(DIGITS_STYLE));
				}
			}
			break;
			case TAG_LONG_ARRAY: {
				long[] raw = raw((NBTTagLongArray) nbt);
				for(int i = 0; i < raw.length; ++i) {
					if(i != 0) ret.appendText(", ");
					ret.appendSibling(new TextComponentString(String.valueOf(raw[i])).setStyle(DIGITS_STYLE))
							.appendSibling(new TextComponentString(type).setStyle(LITERAL_STYLE));
				}
			}
			break;
		}
		return ret.appendText("]");
	}

	private static ITextComponent highlight(NBTPrimitive base) {
		String value = "", suffix = "";
		switch(base.getId()) {
			case TAG_BYTE:
				value = String.valueOf(base.getByte());
				suffix = "b";
				break;
			case TAG_SHORT:
				value = String.valueOf(base.getShort());
				suffix = "s";
				break;
			case TAG_INT:
				value = String.valueOf(base.getInt());
				break;
			case TAG_LONG:
				value = String.valueOf(base.getLong());
				suffix = "L";
				break;
			case TAG_FLOAT:
				value = String.valueOf(base.getFloat());
				suffix = "f";
				break;
			case TAG_DOUBLE:
				value = String.valueOf(base.getDouble());
				suffix = "d";
				break;
		}

		return new TextComponentString(value).setStyle(DIGITS_STYLE)
				.appendSibling(new TextComponentString(suffix).setStyle(LITERAL_STYLE));
	}
}