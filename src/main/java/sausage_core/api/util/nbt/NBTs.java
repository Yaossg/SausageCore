package sausage_core.api.util.nbt;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class NBTs {
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
        for (NBTBase each : arg)
            list.appendTag(each);
        return list;
    }

    public static NBTTagList stringListOf(Iterable<String> arg) {
        NBTTagList list = new NBTTagList();
        for (String each : arg)
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
        for (Map.Entry<String, NBTBase> entry : arg.entrySet())
            map.setTag(entry.getKey(), entry.getValue());
        return map;
    }

    public static NBTTagCompound stringListOf(Map<String, String> arg) {
        NBTTagCompound map = new NBTTagCompound();
        for (Map.Entry<String, String> entry : arg.entrySet())
            map.setTag(entry.getKey(), of(entry.getValue()));
        return map;
    }

    public static NBTTagList asList(byte... args) {
        NBTTagList list = new NBTTagList();
        for (byte each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(short... args) {
        NBTTagList list = new NBTTagList();
        for (short each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(int... args) {
        NBTTagList list = new NBTTagList();
        for (int each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(long... args) {
        NBTTagList list = new NBTTagList();
        for (long each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(float... args) {
        NBTTagList list = new NBTTagList();
        for (float each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(double... args) {
        NBTTagList list = new NBTTagList();
        for (double each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(String... args) {
        NBTTagList list = new NBTTagList();
        for (String each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(byte[]... args) {
        NBTTagList list = new NBTTagList();
        for (byte[] each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(int[]... args) {
        NBTTagList list = new NBTTagList();
        for (int[] each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(long[]... args) {
        NBTTagList list = new NBTTagList();
        for (long[] each : args)
            list.appendTag(of(each));
        return list;
    }

    public static NBTTagList asList(NBTBase... args) {
        NBTTagList list = new NBTTagList();
        for (NBTBase each : args)
            list.appendTag(each);
        return list;
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

    /**
     * @deprecated
     * @see NBTUtil#createPosTag(BlockPos)
     * @see NBTUtil#getPosFromTag(NBTTagCompound)
     * */
    @Deprecated
    public static class BlockPosNBTs {
        @Deprecated
        public static BlockPos read(NBTTagCompound compound) {
            return new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
        }
        @Deprecated
        public static NBTTagCompound write(BlockPos pos) {
            return NBTs.of(
                    "x", NBTs.of(pos.getX()),
                    "y", NBTs.of(pos.getY()),
                    "z", NBTs.of(pos.getZ()));
        }
    }

}