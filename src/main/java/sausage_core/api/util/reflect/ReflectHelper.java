package sausage_core.api.util.reflect;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;
import static sausage_core.api.util.common.SausageUtils.rawtype;

/**
 * Purified Simplified {@link net.minecraftforge.fml.relauncher.ReflectionHelper}
 * */
public final class ReflectHelper {
	private ReflectHelper() {}
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
		Class<?> clazz = array.getClass();
		if (clazz == Byte[].class) {
			return toPrimitive((Byte[]) array);
		} else if (clazz == Character[].class) {
			return toPrimitive((Character[]) array);
		} else if (clazz == Double[].class) {
			return toPrimitive((Double[]) array);
		} else if (clazz == Float[].class) {
			return toPrimitive((Float[]) array);
		} else if (clazz == Integer[].class) {
			return toPrimitive((Integer[]) array);
		} else if (clazz == Long[].class) {
			return toPrimitive((Long[]) array);
		} else if (clazz == Short[].class) {
			return toPrimitive((Short[]) array);
		} else if (clazz == Boolean[].class) {
			return toPrimitive((Boolean[]) array);
		}
		return array;
	}
}
