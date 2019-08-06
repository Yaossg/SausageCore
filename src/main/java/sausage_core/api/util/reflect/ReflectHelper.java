package sausage_core.api.util.reflect;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;
import static sausage_core.api.util.common.SausageUtils.rawtype;

/**
 * Purified Simplified {@link net.minecraftforge.fml.relauncher.ReflectionHelper}
 * */
public final class ReflectHelper {
	public static Field getAccessibleField(Class<?> clazz, String name)
			throws NoSuchFieldException {
		Field f = clazz.getDeclaredField(name);
		f.setAccessible(true);
		return f;
	}

	public static <T> T get(Class<?> clazz, Object instance, String name)
			throws NoSuchFieldException, IllegalAccessException {
		return rawtype(getAccessibleField(clazz, name).get(instance));
	}

	public static <T> void set(Class<T> clazz, T instance, String name, Object value)
			throws NoSuchFieldException, IllegalAccessException {
		getAccessibleField(clazz, name).set(instance, value);
	}

	public static Object toPrimitiveArray(Object array) {
		if (array == null) return null;
		Class<?> ct = array.getClass().getComponentType();
		Class<?> pt = ClassUtils.wrapperToPrimitive(ct);
		switch (EnumPrimitive.match(pt)) {
			case BOOLEAN:
				return toPrimitive((Boolean[]) array);
			case BYTE:
				return toPrimitive((Byte[]) array);
			case CHAR:
				return toPrimitive((Character[]) array);
			case SHORT:
				return toPrimitive((Short[]) array);
			case INT:
				return toPrimitive((Integer[]) array);
			case LONG:
				return toPrimitive((Long[]) array);
			case FLOAT:
				return toPrimitive((Float[]) array);
			case DOUBLE:
				return toPrimitive((Double[]) array);
		}
		return array;
	}
}
