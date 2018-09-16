package com.github.yaossg.sausage_core.api.util.nbt;

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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public final class NBTFactoryManager {
    private static final Map<Class<?>, Function> to = new IdentityHashMap<>();
    private static final Map<Class<?>, Function> from = new IdentityHashMap<>();
    @SuppressWarnings("unchecked")
    public static <T, R> void register(Class<T> clazz, Function<T, R> to, Function<R, T> from) {
        NBTFactoryManager.to.put(clazz, to);
        NBTFactoryManager.from.put(clazz, from);
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
        register(byte[].class, NBTs::of, NBTTagByteArray::getByteArray);
        register(ByteList.class, NBTs::of, (NBTTagByteArray arg) -> ByteArrayList.wrap(arg.getByteArray()));
        register(int[].class, NBTs::of, NBTTagIntArray::getIntArray);
        register(IntList.class, NBTs::of, (NBTTagIntArray arg) -> IntArrayList.wrap(arg.getIntArray()));
        register(long[].class, NBTs::of, (NBTTagLongArray nbt) -> nbt.data);
        register(LongList.class, NBTs::of, (NBTTagLongArray arg) -> LongArrayList.wrap(arg.data));
        register(char[].class, chars -> NBTs.of(String.valueOf(chars)), (NBTTagString nbt) -> nbt.getString().toCharArray());
        register(CharList.class, chars -> NBTs.of(String.valueOf(chars.toCharArray())), (NBTTagString nbt) -> CharArrayList.wrap(nbt.getString().toCharArray()));
        register(String.class, NBTs::of, NBTTagString::getString);
        register(NBTBase.class, Function.identity(), Function.identity());
        register(UUID.class, NBTUtil::createUUIDTag, NBTUtil::getUUIDFromTag);
        register(BlockPos.class, NBTUtil::createPosTag, NBTUtil::getPosFromTag);

    }

    @SuppressWarnings("unchecked")
    public static <T> NBTTagCompound toNBT(Class<T> clazz, T instance) {
        checkArgument(clazz.isAnnotationPresent(NBTFactory.class));
        NBTTagCompound nbt = new NBTTagCompound();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                if (!Modifier.isPublic(field.getModifiers()))
                    continue;
                if (Modifier.isStatic(field.getModifiers()))
                    continue;
                if (field.isAnnotationPresent(NBTFactory.Ignore.class))
                    continue;
                NBTBase nbtBase;
                Object fieldValue = field.get(instance);
                if (field.isAnnotationPresent(NBTFactory.class)) {
                    nbtBase = toNBT((Class) fieldValue.getClass(), fieldValue);
                } else {
                    Function function = null;
                    for (Map.Entry<Class<?>, Function> entry : to.entrySet())
                        if(entry.getKey().isInstance(fieldValue)) {
                            function = entry.getValue();
                            break;
                        }
                    if(function == null) throw new Exception();
                    nbtBase = (NBTBase) function.apply(fieldValue);
                }
                String name = field.getName();
                if(field.isAnnotationPresent(NBTFactory.Rename.class))
                    name = field.getAnnotation(NBTFactory.Rename.class).value();
                nbt.setTag(name, nbtBase);
            } catch (Exception e) {
                //NO OP
            }
        }
        return nbt;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromNBT(Class<T> clazz, NBTTagCompound nbt) {
        checkArgument(clazz.isAnnotationPresent(NBTFactory.class));
        try {
            T object = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    if (!Modifier.isPublic(field.getModifiers()))
                        continue;
                    if (Modifier.isStatic(field.getModifiers()))
                        continue;
                    if (field.isAnnotationPresent(NBTFactory.Ignore.class))
                        continue;
                    String name = field.getName();
                    if(field.isAnnotationPresent(NBTFactory.Rename.class))
                        name = field.getAnnotation(NBTFactory.Rename.class).value();
                    NBTBase tag = nbt.getTag(name);
                    if (field.isAnnotationPresent(NBTFactory.class)) {
                        field.set(object, fromNBT((Class) field.getType(), (NBTTagCompound) tag));
                    } else {
                        Function function = null;
                        for (Map.Entry<Class<?>, Function> entry : from.entrySet())
                            if(entry.getKey().isAssignableFrom(field.getType())) {
                                function = entry.getValue();
                                break;
                            }
                        if(function == null) throw new Exception();
                        field.set(object, function.apply(tag));
                    }
                } catch (Exception e) {
                    // NO OP
                }
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }
}

