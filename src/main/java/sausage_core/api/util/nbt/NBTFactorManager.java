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

import static com.google.common.base.Preconditions.checkNotNull;
import static sausage_core.api.util.common.SausageUtils.rawtype;

@Beta
public final class NBTFactorManager {
    static final Map<Class<?>, Function> mapToNBT = new IdentityHashMap<>();
    static final Map<Class<?>, Function> mapFromNBT = new IdentityHashMap<>();
    static final Set<Class<?>> arrayTypes = new HashSet<>();
    @SuppressWarnings("unchecked")
    public static <T, R> void register(Class<T> clazz, Function<T, R> to, Function<R, T> from) {
        NBTFactorManager.mapToNBT.put(clazz, to);
        NBTFactorManager.mapFromNBT.put(clazz, from);
    }
    public static <T, R> void registerArrayType(Class<T> clazz, Function<T, R> to, Function<R, T> from) {
        register(clazz, to, from);
        arrayTypes.add(clazz);
    }

    static boolean isArray(Class<?> clazz) {
        return clazz.isArray() || arrayTypes.contains(clazz);
    }

    static {
        register(byte.class, NBTs::of, NBTTagByte::getByte);
        register(short.class, NBTs::of, NBTTagShort::getShort);
        register(int.class, NBTs::of, NBTTagInt::getInt);
        register(long.class, NBTs::of, NBTTagLong::getLong);
        register(float.class, NBTs::of, NBTTagFloat::getFloat);
        register(double.class, NBTs::of, NBTTagDouble::getDouble);
        register(char.class, NBTs::of, (NBTTagString nbt) -> nbt.getString().toCharArray()[0]);
        register(boolean.class, NBTs::of, (NBTTagByte nbt) -> nbt.getByte() != 0);
        register(Byte.class, NBTs::of, NBTTagByte::getByte);
        register(Short.class, NBTs::of, NBTTagShort::getShort);
        register(Integer.class, NBTs::of, NBTTagInt::getInt);
        register(Long.class, NBTs::of, NBTTagLong::getLong);
        register(Float.class, NBTs::of, NBTTagFloat::getFloat);
        register(Double.class, NBTs::of, NBTTagDouble::getDouble);
        register(Character.class, NBTs::of, (NBTTagString nbt) -> nbt.getString().toCharArray()[0]);
        register(Boolean.class, NBTs::of, (NBTTagByte nbt) -> nbt.getByte() != 0);
        registerArrayType(byte[].class, NBTs::of, NBTTagByteArray::getByteArray);
        registerArrayType(ByteList.class, NBTs::of, (NBTTagByteArray arg) -> ByteArrayList.wrap(arg.getByteArray()));
        registerArrayType(int[].class, NBTs::of, NBTTagIntArray::getIntArray);
        registerArrayType(IntList.class, NBTs::of, (NBTTagIntArray arg) -> IntArrayList.wrap(arg.getIntArray()));
        registerArrayType(long[].class, NBTs::of, (NBTTagLongArray nbt) -> nbt.data);
        registerArrayType(LongList.class, NBTs::of, (NBTTagLongArray arg) -> LongArrayList.wrap(arg.data));
        registerArrayType(char[].class, chars -> NBTs.of(String.valueOf(chars)), (NBTTagString nbt) -> nbt.getString().toCharArray());
        registerArrayType(CharList.class, chars -> NBTs.of(String.valueOf(chars.toCharArray())), (NBTTagString nbt) -> CharArrayList.wrap(nbt.getString().toCharArray()));
        register(String.class, NBTs::of, NBTTagString::getString);
        register(NBTBase.class, Function.identity(), Function.identity());
        register(UUID.class, NBTUtil::createUUIDTag, NBTUtil::getUUIDFromTag);
        register(BlockPos.class, NBTUtil::createPosTag, NBTUtil::getPosFromTag);
    }

    static Function findToNBT(Object value) {
        return mapToNBT.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(value))
                .findAny().map(Map.Entry::getValue).orElse(null);
    }

    @SuppressWarnings("unchecked")
    static NBTBase toNBT(Object value) {
        return rawtype(checkNotNull(findToNBT(value).apply(value)));
    }

    static Function findFromNBT(Class<?> type) {
        return mapFromNBT.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(type))
                .findAny().map(Map.Entry::getValue).orElse(null);
    }

    @SuppressWarnings("unchecked")
    static Object fromNBT(Class<?> type, Object value) {
        return checkNotNull(findFromNBT(type).apply(value));
    }
}

