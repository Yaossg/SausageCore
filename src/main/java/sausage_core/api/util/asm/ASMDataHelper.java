package sausage_core.api.util.asm;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import sausage_core.api.util.reflect.ReflectHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sausage_core.api.util.common.SausageUtils.rawtype;

public final class ASMDataHelper {
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A toAnnotation(Class<A> annotationClass, Map<String, Object> data) {
		Map<String, Object> map = new HashMap<>();
		Method[] methods = annotationClass.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			Object o = data.get(name);
			if(o == null) {
				o = method.getDefaultValue();
			} else {
				Class type = method.getReturnType();
				if (type.isEnum()) o = toEnum(type, o);
				if (type.isArray()) {
					Class ct = type.getComponentType();
					List list = (List) o;
					if (ct.isEnum())
						list = (List) list.stream().map(e -> toEnum(ct, e)).collect(Collectors.toList());
					o = Arrays.copyOf(list.toArray(), list.size(), type);
					if (ct.isPrimitive()) o = ReflectHelper.toPrimitiveArray(o);
				}
			}
			map.put(name, o);
		}
		return (A) Proxy.newProxyInstance(
				annotationClass.getClassLoader(),
				new Class<?>[]{annotationClass},
				(proxy, method, args) -> map.get(method.getName())
		);
	}

	public static <T extends Enum<T>> T toEnum(Class<T> enumClass, Object o) {
		ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) o;
		return rawtype(Enum.valueOf(enumClass, holder.getValue()));
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
