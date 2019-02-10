package sausage_core.api.util.nbt;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static sausage_core.api.util.nbt.NBTFactorManager.isArray;

@Beta
public class NBTFactorContext<T> {
    private final Class<T> clazz;
    private final Map<String, Field> commons;
    private final Map<String, Field> lists;
    private final Map<String, Field> factories;
    private final Map<String, NBTFactorContext> contexts;

    public static <T> NBTFactorContext<T> parse(Class<T> clazz) {
        return new NBTFactorContext<>(clazz);
    }

    private NBTFactorContext(Class<T> clazz) {
        checkArgument(clazz.isAnnotationPresent(NBTFactor.class));
        this.clazz = clazz;
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> !field.isAnnotationPresent(NBTFactor.Ignore.class))
                .collect(Collectors.toList());
        Set<String> names = new HashSet<>();
        ImmutableMap.Builder<String, Field> commons = ImmutableMap.builder();
        ImmutableMap.Builder<String, Field> lists = ImmutableMap.builder();
        ImmutableMap.Builder<String, Field> factories = ImmutableMap.builder();
        ImmutableMap.Builder<String, NBTFactorContext> contexts = ImmutableMap.builder();
        for (Field field : fields) {
            String name = field.getName();
            if(field.isAnnotationPresent(NBTFactor.Rename.class))
                name = field.getAnnotation(NBTFactor.Rename.class).value();
            checkArgument(!name.isEmpty());
            checkArgument(names.add(name));
            if(field.isAnnotationPresent(NBTFactor.AsList.class)
                    && isArray(field.getType())) {
                if(field.isAnnotationPresent(NBTFactor.class))
                    contexts.put(name, parse(field.getType().getComponentType()));
                lists.put(name, field);
            } else if(field.isAnnotationPresent(NBTFactor.class)) {
                contexts.put(name, parse(field.getType()));
                factories.put(name, field);
            } else {
                commons.put(name, field);
            }
        }
        this.commons = commons.build();
        this.lists = lists.build();
        this.factories = factories.build();
        this.contexts = contexts.build();
    }

    interface ExRunnable {
        void run() throws Exception;
    }

    void ignoreException(ExRunnable runnable) {
        try {
            runnable.run();
        } catch (NullPointerException e) {
            //NO OP
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public NBTTagCompound toNBT(T instance) {
        checkNotNull(instance);
        NBTTagCompound nbt = new NBTTagCompound();
        commons.forEach((name, field) -> ignoreException(() ->
                nbt.setTag(name, NBTFactorManager.toNBT(field.get(instance)))));
        lists.forEach((name, field) -> ignoreException(() -> {
            Object value = field.get(instance);
            boolean isFactory = field.isAnnotationPresent(NBTFactor.class);
            if(isArray(value.getClass())) {
                int length = Array.getLength(value);
                NBTTagList bases = new NBTTagList();
                for (int i = 0; i < length; ++i) {
                    Object object = Array.get(value, i);
                    bases.appendTag(isFactory ? contexts.get(name).toNBT(object) : NBTFactorManager.toNBT(object));
                }
                nbt.setTag(name, bases);
            }
        }));
        factories.forEach((name, field) -> ignoreException(() ->
                nbt.setTag(name, contexts.get(name).toNBT(field.get(instance)))));
        return nbt;
    }

    private Object fromNBT0(NBTBase nbt) {
        return fromNBT((NBTTagCompound) nbt);
    }

    @SuppressWarnings("unchecked")
    public T fromNBT(NBTTagCompound nbt) {
        checkNotNull(nbt);
        T object;
        try {
            object = clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
        commons.forEach((name, field) -> ignoreException(() ->
                field.set(object, NBTFactorManager.fromNBT(field.getType(), nbt.getTag(name)))));
        lists.forEach((name, field) -> ignoreException(() -> {
            Class<?> type = field.getType();
            NBTTagList tag = checkNotNull((NBTTagList) nbt.getTag(name));
            List objects = NBTs.stream(tag).map(field.isAnnotationPresent(NBTFactor.class)
                    ? contexts.get(name)::fromNBT0
                    : nbt0 -> NBTFactorManager.fromNBT(type.getComponentType(), nbt0)).collect(Collectors.toList());
            if(isArray(type))
                field.set(object, objects.toArray((Object[]) Array.newInstance(type.getComponentType(), 0)));
        }));
        factories.forEach((name, field) -> ignoreException(() ->
                field.set(object, contexts.get(name).fromNBT0(nbt.getTag(name)))));
        return object;
    }

//    public static void main(String[] args) {
//        NBTFactoryContext<Factory> context = parse(Factory.class);
//        Factory instance = new Factory();
//        System.out.println(context.fromNBT(context.toNBT(instance)));
//    }
//
//    @NBTFactory
//    public static class Factory {
//
//        @NBTFactory
//        public static class Inner {
//
//            @Override
//            public String toString() {
//                return new ReflectionToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE).toString();
//            }
//        }
//
//        @Override
//        public String toString() {
//            return new ReflectionToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE).toString();
//        }
//    }
}
