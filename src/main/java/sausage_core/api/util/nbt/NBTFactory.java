package sausage_core.api.util.nbt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * read/write all public non-static fields
 * @see NBTFactoryManager
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface NBTFactory {

    /**
     * declares to ignore this field
     * */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Ignore {}

    /**
     * overrides field name to read/write
     * */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Rename {
        String value();
    }

    /**
     * considers int[] as {@link net.minecraft.nbt.NBTTagList}{@literal <}Integer> instead of {@link net.minecraft.nbt.NBTTagIntArray}
     * also necessary when using array with {@link NBTFactory}
     * */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface AsList {}

    /**
     * marks elements' type of {@link java.util.List}
     * */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ListElement {
        Class<?> value();
    }



}