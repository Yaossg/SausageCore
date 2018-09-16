package com.github.yaossg.sausage_core.api.util.nbt;

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
     * manager ignores this field
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

}