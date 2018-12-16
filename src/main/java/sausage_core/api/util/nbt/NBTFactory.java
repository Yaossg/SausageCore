package sausage_core.api.util.nbt;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * read/write all public non-static fields
 * @see NBTFactoryManager
 * */
@Beta
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
     * considers byte[], int[], long[] as {@link net.minecraft.nbt.NBTTagList}
     *     instead of {@link net.minecraft.nbt.NBTTagByteArray}, {@link net.minecraft.nbt.NBTTagIntArray}, {@link net.minecraft.nbt.NBTTagLongArray}
     * also necessary when using array with {@link NBTFactory}
     * */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface AsList {}

}