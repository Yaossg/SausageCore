package yaossg.mod.sausage_core.api.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import net.minecraft.nbt.*;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NBTs {
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
    public static NBTTagByteArray array(byte... arg) {
        return of(arg);
    }
    public static NBTTagIntArray array(int... arg) {
        return of(arg);
    }
    public static NBTTagLongArray array(long... arg) {
        return of(arg);
    }

    public static NBTTagList of(Iterable<NBTBase> arg) {
        NBTTagList list = new NBTTagList();
        for(NBTBase each : arg)
            list.appendTag(each);
        return list;
    }
    public static NBTTagList ofStrings(Iterable<String> arg) {
        NBTTagList list = new NBTTagList();
        for(String each : arg)
            list.appendTag(of(each));
        return list;
    }
    public static NBTTagList ofByteArrays(Iterable<byte[]> arg) {
        NBTTagList list = new NBTTagList();
        for(byte[] each : arg)
            list.appendTag(of(each));
        return list;
    }
    public static NBTTagList ofIntArrays(Iterable<int[]> arg) {
        NBTTagList list = new NBTTagList();
        for(int[] each : arg)
            list.appendTag(of(each));
        return list;
    }
    public static NBTTagList ofLongArrays(Iterable<long[]> arg) {
        NBTTagList list = new NBTTagList();
        for(long[] each : arg)
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
        for (Map.Entry<String, NBTBase> entry :arg.entrySet())
            map.setTag(entry.getKey(), entry.getValue());
        return map;
    }
    public static NBTTagCompound ofStrings(Map<String, String> arg) {
        NBTTagCompound map = new NBTTagCompound();
        for (Map.Entry<String, String> entry :arg.entrySet())
            map.setTag(entry.getKey(), of(entry.getValue()));
        return map;
    }
    public static NBTTagCompound ofByteArrays(Map<String, byte[]> arg) {
        NBTTagCompound map = new NBTTagCompound();
        for (Map.Entry<String, byte[]> entry :arg.entrySet())
            map.setTag(entry.getKey(), of(entry.getValue()));
        return map;
    }
    public static NBTTagCompound ofIntArrays(Map<String, int[]> arg) {
        NBTTagCompound map = new NBTTagCompound();
        for (Map.Entry<String, int[]> entry :arg.entrySet())
            map.setTag(entry.getKey(), of(entry.getValue()));
        return map;
    }
    public static NBTTagCompound ofLongArrays(Map<String, long[]> arg) {
        NBTTagCompound map = new NBTTagCompound();
        for (Map.Entry<String, long[]> entry :arg.entrySet())
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
    public static Stream<NBTBase> stream(NBTTagList list) {
        return Streams.stream(list);
    }
    public static Map<String, NBTBase> map(NBTTagCompound compound) {
        return compound.getKeySet().stream().collect(Collectors.toMap(Function.identity(), compound::getTag));
    }
    public static Stream<NBTBase> values(NBTTagCompound compound) {
        return compound.getKeySet().stream().map(compound::getTag);
    }
    public static Set<Map.Entry<String, NBTBase>> entrySet(NBTTagCompound compound) {
        return map(compound).entrySet();
    }
}