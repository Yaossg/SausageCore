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
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Beta
public class NBTFactoryContext<T> {
    private final Class<T> clazz;
    private final Map<String, Field> commons;
    private final Map<String, Field> lists;
    private final Map<String, Field> factories;
    private final Map<String, NBTFactoryContext> contexts;

    public static <T> NBTFactoryContext<T> parse(Class<T> clazz) {
        return new NBTFactoryContext<>(clazz);
    }

    private NBTFactoryContext(Class<T> clazz) {
        checkArgument(clazz.isAnnotationPresent(NBTFactory.class));
        this.clazz = clazz;
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> !field.isAnnotationPresent(NBTFactory.Ignore.class))
                .collect(Collectors.toList());
        Set<String> names = new HashSet<>();
        ImmutableMap.Builder<String, Field> commons = ImmutableMap.builder();
        ImmutableMap.Builder<String, Field> lists = ImmutableMap.builder();
        ImmutableMap.Builder<String, Field> factories = ImmutableMap.builder();
        ImmutableMap.Builder<String, NBTFactoryContext> contexts = ImmutableMap.builder();
        for (Field field : fields) {
            String name = field.getName();
            if(field.isAnnotationPresent(NBTFactory.Rename.class))
                name = field.getAnnotation(NBTFactory.Rename.class).value();
            checkArgument(!name.isEmpty());
            checkArgument(names.add(name));
            if(field.isAnnotationPresent(NBTFactory.AsList.class) && NBTFactoryManager.isArray(field.getType())) {
                if(field.isAnnotationPresent(NBTFactory.class))
                    contexts.put(name, parse(field.getType().getComponentType()));
                lists.put(name, field);
            } else if(field.isAnnotationPresent(NBTFactory.class)) {
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

    @SuppressWarnings("unchecked")
    private NBTBase toNBTSingleton(Object value) {
        Function function = null;
        checkNotNull(value);
        for (Map.Entry<Class<?>, Function> entry : NBTFactoryManager.mapToNBT.entrySet())
            if(entry.getKey().isInstance(value)) {
                function = entry.getValue();
                break;
            }
        return (NBTBase) checkNotNull(checkNotNull(function).apply(value));
    }

    @SuppressWarnings("unchecked")
    public NBTTagCompound toNBT(T instance) {
        checkNotNull(instance);
        NBTTagCompound nbt = new NBTTagCompound();
        for (Map.Entry<String, Field> common : commons.entrySet()) {
            try {
                nbt.setTag(common.getKey(), toNBTSingleton(common.getValue().get(instance)));
            } catch (NullPointerException e) {
                //NO OP
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        for (Map.Entry<String, Field> list : lists.entrySet()) {
            try {
                Object value = checkNotNull(list.getValue().get(instance));
                boolean isFactory = list.getValue().isAnnotationPresent(NBTFactory.class);
                if(NBTFactoryManager.isArray(value.getClass())) {
                    int length = Array.getLength(value);
                    if(length == 0) {
                        nbt.setTag(list.getKey(), new NBTTagList());
                    } else {
                        NBTTagList bases = new NBTTagList();
                        for (int i = 0; i < length; ++i) {
                            Object o = Array.get(value, i);
                            bases.appendTag(isFactory ? contexts.get(list.getKey()).toNBT(o) : toNBTSingleton(o));
                        }
                        nbt.setTag(list.getKey(), bases);
                    }
                }
            } catch (NullPointerException e) {
                //NO OP
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        for (Map.Entry<String, Field> factory : factories.entrySet()) {
            try {
                Object value = checkNotNull(factory.getValue().get(instance));
                String name = factory.getKey();
                nbt.setTag(name, contexts.get(name).toNBT(value));
            } catch (NullPointerException e) {
                //NO OP
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return nbt;
    }

    private Object fromNBT0(NBTBase nbt) {
        return fromNBT((NBTTagCompound) nbt);
    }

    @SuppressWarnings("unchecked")
    private Object fromNBTSingleton(NBTBase nbt, Class<?> type) {
        Function function = null;
        for (Map.Entry<Class<?>, Function> entry : NBTFactoryManager.mapFromNBT.entrySet())
            if(entry.getKey().isAssignableFrom(type)) {
                function = entry.getValue();
                break;
            }
        return checkNotNull(function).apply(nbt);
    }

    @SuppressWarnings("unchecked")
    public T fromNBT(NBTTagCompound nbt) {
        checkNotNull(nbt);
        try {
            T object = clazz.newInstance();
            for (Map.Entry<String, Field> common : commons.entrySet()) {
                Field field = common.getValue();
                field.set(object, fromNBTSingleton(checkNotNull(nbt.getTag(common.getKey())), field.getType()));
            }
            for (Map.Entry<String, Field> list : lists.entrySet()) {
                Field field = list.getValue();
                Class<?> type = field.getType();
                NBTTagList tag = checkNotNull((NBTTagList) nbt.getTag(list.getKey()));
                List objects = NBTs.stream(tag).map(field.isAnnotationPresent(NBTFactory.class)
                        ? contexts.get(list.getKey())::fromNBT0
                        : nbt0 -> fromNBTSingleton(nbt0, type.getComponentType())).collect(Collectors.toList());
                if(NBTFactoryManager.isArray(type))
                    field.set(object, objects.toArray((Object[]) Array.newInstance(type.getComponentType(), 0)));
            }
            for (Map.Entry<String, Field> factory : factories.entrySet()) {
                try {
                    factory.getValue().set(object, contexts.get(factory.getKey()).fromNBT((NBTTagCompound) nbt.getTag(factory.getKey())));
                } catch (NullPointerException e) {
                    //NO OP
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
//        @NBTFactory.AsList
//        IntList[] sq = {IntArrayList.wrap(new int[] {1, 2}), IntArrayList.wrap(new int[] {3, 4})};
//        @NBTFactory
//        public static class Inner {
//            public int d = 0;
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
