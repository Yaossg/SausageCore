package sausage_core.api.util.nbt;

import com.google.common.annotations.Beta;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Function;

@Beta
public final class NBTFactoryManager {
    static final Map<Class<?>, Function> mapToNBT = new IdentityHashMap<>();
    static final Map<Class<?>, Function> mapFromNBT = new IdentityHashMap<>();
    static final Set<Class<?>> arrayTypes = new HashSet<>();
    @SuppressWarnings("unchecked")
    public static <T, R> void registerNBTFactory(Class<T> clazz, Function<T, R> to, Function<R, T> from) {
        NBTFactoryManager.mapToNBT.put(clazz, to);
        NBTFactoryManager.mapFromNBT.put(clazz, from);
    }
    public static <T, R> void registerNBTFactoryArray(Class<T> clazz, Function<T, R> to, Function<R, T> from) {
        registerNBTFactory(clazz, to, from);
        arrayTypes.add(clazz);
    }

    static boolean isArray(Class<?> clazz) {
        return clazz.isArray() || arrayTypes.contains(clazz);
    }

    static {
        registerNBTFactory(byte.class, NBTs::of, NBTTagByte::getByte);
        registerNBTFactory(short.class, NBTs::of, NBTTagShort::getShort);
        registerNBTFactory(int.class, NBTs::of, NBTTagInt::getInt);
        registerNBTFactory(long.class, NBTs::of, NBTTagLong::getLong);
        registerNBTFactory(float.class, NBTs::of, NBTTagFloat::getFloat);
        registerNBTFactory(double.class, NBTs::of, NBTTagDouble::getDouble);
        registerNBTFactory(char.class, NBTs::of, (NBTTagString nbt) -> nbt.getString().toCharArray()[0]);
        registerNBTFactory(boolean.class, NBTs::of, (NBTTagByte nbt) -> nbt.getByte() != 0);
        registerNBTFactory(Byte.class, NBTs::of, NBTTagByte::getByte);
        registerNBTFactory(Short.class, NBTs::of, NBTTagShort::getShort);
        registerNBTFactory(Integer.class, NBTs::of, NBTTagInt::getInt);
        registerNBTFactory(Long.class, NBTs::of, NBTTagLong::getLong);
        registerNBTFactory(Float.class, NBTs::of, NBTTagFloat::getFloat);
        registerNBTFactory(Double.class, NBTs::of, NBTTagDouble::getDouble);
        registerNBTFactory(Character.class, NBTs::of, (NBTTagString nbt) -> nbt.getString().toCharArray()[0]);
        registerNBTFactory(Boolean.class, NBTs::of, (NBTTagByte nbt) -> nbt.getByte() != 0);
        registerNBTFactoryArray(byte[].class, NBTs::of, NBTTagByteArray::getByteArray);
        registerNBTFactoryArray(ByteList.class, NBTs::of, (NBTTagByteArray arg) -> ByteArrayList.wrap(arg.getByteArray()));
        registerNBTFactoryArray(int[].class, NBTs::of, NBTTagIntArray::getIntArray);
        registerNBTFactoryArray(IntList.class, NBTs::of, (NBTTagIntArray arg) -> IntArrayList.wrap(arg.getIntArray()));
        registerNBTFactoryArray(long[].class, NBTs::of, (NBTTagLongArray nbt) -> nbt.data);
        registerNBTFactoryArray(LongList.class, NBTs::of, (NBTTagLongArray arg) -> LongArrayList.wrap(arg.data));
        registerNBTFactoryArray(char[].class, chars -> NBTs.of(String.valueOf(chars)), (NBTTagString nbt) -> nbt.getString().toCharArray());
        registerNBTFactoryArray(CharList.class, chars -> NBTs.of(String.valueOf(chars.toCharArray())), (NBTTagString nbt) -> CharArrayList.wrap(nbt.getString().toCharArray()));
        registerNBTFactory(String.class, NBTs::of, NBTTagString::getString);
        registerNBTFactory(NBTBase.class, Function.identity(), Function.identity());
        registerNBTFactory(UUID.class, NBTUtil::createUUIDTag, NBTUtil::getUUIDFromTag);
        registerNBTFactory(BlockPos.class, NBTUtil::createPosTag, NBTUtil::getPosFromTag);
    }
}

