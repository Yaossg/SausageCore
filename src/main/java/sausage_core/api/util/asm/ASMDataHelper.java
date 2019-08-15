package sausage_core.api.util.asm;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import org.objectweb.asm.Type;
import sausage_core.api.util.reflect.ReflectHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.objectweb.asm.Type.*;
import static sausage_core.api.util.common.SausageUtils.rawtype;

public final class ASMDataHelper {
	private ASMDataHelper() {}

	/**
	 * Create an annotation that represents original data
	 * */
	public static <A extends Annotation> A toAnnotation(Class<A> annotationClass, Map<String, Object> data) {
		return toAnnotation(annotationClass, data, true);
	}

	/**
	 * Create an annotation that represents original data
	 * @param initialize whether initialize classes in the properties
	 * */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A toAnnotation(Class<A> annotationClass, Map<String, Object> data, 
														boolean initialize) {
		Map<String, Object> map = new HashMap<>();
		ClassLoader classLoader = annotationClass.getClassLoader();
		Method[] methods = annotationClass.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			Object o = data.get(name);
			if(o == null) {
				o = method.getDefaultValue();
			} else {
				Class type = method.getReturnType();
				if (type == Class.class) o = toClass(o, initialize, classLoader);
				else if (type.isEnum()) o = toEnum(type, o);
				else if (type.isArray()) {
					Class ct = type.getComponentType();
					List list = (List) o;
					if (ct == Class.class)
						list = (List) list.stream().map(e -> toClass(e, initialize, classLoader)).collect(Collectors.toList());
					else if (ct.isEnum())
						list = (List) list.stream().map(e -> toEnum(ct, e)).collect(Collectors.toList());
					o = Arrays.copyOf(list.toArray(), list.size(), type);
					if (ct.isPrimitive()) o = ReflectHelper.toPrimitiveArray(o);
				}
			}
			map.put(name, o);
		}
		return annotationForMap(annotationClass, map);
	}

	public static <A extends Annotation> A annotationForMap(Class<A> annotationClass, Map<String, Object> map) {
		return AccessController.doPrivileged((PrivilegedAction<A>)
				new AnnotationInvocationHandler<>(annotationClass, map)::toAnnotation);
	}

	public static <T extends Enum<T>> T toEnum(Class<T> enumClass, Object o) {
		ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) o;
		return rawtype(Enum.valueOf(enumClass, holder.getValue()));
	}

	public static Class toClass(Object o, boolean initialize, ClassLoader classLoader) {
		Type type = (Type) o;
		String name = type.getClassName();
		switch (type.getSort()) {
			case VOID:    return void.class;
			case BOOLEAN: return boolean.class;
			case CHAR:    return char.class;
			case BYTE:    return byte.class;
			case SHORT:   return short.class;
			case INT:     return int.class;
			case FLOAT:   return float.class;
			case LONG:    return long.class;
			case DOUBLE:  return double.class;
			case ARRAY:   name = type.getDescriptor().replace('/', '.');
			case OBJECT:  default:
		}
		try {
			return Class.forName(name, initialize, classLoader);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static <T> T getField(ASMDataTable.ASMData data)
			throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
		return ReflectHelper.get(Class.forName(data.getClassName()), null, data.getObjectName());
	}

	public static void setField(ASMDataTable.ASMData data, Object value)
			throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
		ReflectHelper.set(Class.forName(data.getClassName()), null, data.getObjectName(), value);
	}
}
