package sausage_core.api.util.asm;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public class AnnotationInvocationHandler<A extends Annotation> implements InvocationHandler {
	private final Class<A> type;
	private final Map<String, Object> memberValues;
	private transient volatile Method[] memberMethods = null;

	@SuppressWarnings("unchecked")
	public A toAnnotation() {
		return (A) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, this);
	}

	public AnnotationInvocationHandler(Class<A> type, Map<String, Object> memberValues) {
		Class[] interfaces = type.getInterfaces();
		if (type.isAnnotation() && interfaces.length == 1 && interfaces[0] == Annotation.class) {
			this.type = type;
			this.memberValues = memberValues;
		} else {
			throw new AnnotationFormatError("Attempt to create proxy for a non-annotation type.");
		}
	}

	public Object invoke(Object proxy, Method method, Object[] args) {
		String methodName = method.getName();
		Class[] parameterTypes = method.getParameterTypes();
		if (methodName.equals("equals") && parameterTypes.length == 1 && parameterTypes[0] == Object.class) {
			return equalsImpl(args[0]);
		} else if (parameterTypes.length != 0) {
			throw new AssertionError("Too many parameters for an annotation method");
		} else {
			switch (methodName) {
				case "toString":
					return toStringImpl();
				case "hashCode":
					return hashCodeImpl();
				case "annotationType":
					return type;
				default:
					Object object = memberValues.get(methodName);
					if (object == null) {
						throw new IncompleteAnnotationException(type, methodName);
					} else {
						if (object.getClass().isArray() && Array.getLength(object) != 0) {
							object = cloneArray(object);
						}
						return object;
					}
			}
		}
	}

	private Object cloneArray(Object array) {
		Class<?> clazz = array.getClass();
		if (clazz == byte[].class) {
			byte[] array0 = (byte[]) array;
			return array0.clone();
		} else if (clazz == char[].class) {
			char[] array0 = (char[]) array;
			return array0.clone();
		} else if (clazz == double[].class) {
			double[] array0 = (double[]) array;
			return array0.clone();
		} else if (clazz == float[].class) {
			float[] array0 = (float[]) array;
			return array0.clone();
		} else if (clazz == int[].class) {
			int[] array0 = (int[]) array;
			return array0.clone();
		} else if (clazz == long[].class) {
			long[] array0 = (long[]) array;
			return array0.clone();
		} else if (clazz == short[].class) {
			short[] array0 = (short[]) array;
			return array0.clone();
		} else if (clazz == boolean[].class) {
			boolean[] array0 = (boolean[]) array;
			return array0.clone();
		} else {
			Object[] array0 = (Object[]) array;
			return array0.clone();
		}
	}

	private String toStringImpl() {
		StringBuilder builder = new StringBuilder(128);
		builder.append('@');
		builder.append(this.type.getName());
		builder.append('(');
		boolean first = true;

		for (Entry<String, Object> entry : memberValues.entrySet()) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(entry.getKey());
			builder.append('=');
			builder.append(memberValueToString(entry.getValue()));
		}

		builder.append(')');
		return builder.toString();
	}

	private static String memberValueToString(Object array) {
		Class<?> clazz = array.getClass();
		if (!clazz.isArray()) {
			return array.toString();
		} else if (clazz == byte[].class) {
			return Arrays.toString((byte[]) array);
		} else if (clazz == char[].class) {
			return Arrays.toString((char[]) array);
		} else if (clazz == double[].class) {
			return Arrays.toString((double[]) array);
		} else if (clazz == float[].class) {
			return Arrays.toString((float[]) array);
		} else if (clazz == int[].class) {
			return Arrays.toString((int[]) array);
		} else if (clazz == long[].class) {
			return Arrays.toString((long[]) array);
		} else if (clazz == short[].class) {
			return Arrays.toString((short[]) array);
		} else {
			return clazz == boolean[].class ? Arrays.toString((boolean[]) array) : Arrays.toString((Object[]) array);
		}
	}

	private boolean equalsImpl(Object o) {
		if (o == this) {
			return true;
		} else if (type.isInstance(o)) {
			return false;
		} else {
			Method[] methods = getMemberMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				Object value = memberValues.get(methodName);
				Object other;
				try {
					other = method.invoke(o);
				} catch (InvocationTargetException e) {
					return false;
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				}
				if (!memberValueEquals(value, other)) {
					return false;
				}
			}
			return true;
		}
	}

	private static boolean memberValueEquals(Object one, Object another) {
		Class<?> clazz = one.getClass();
		if (!clazz.isArray()) {
			return one.equals(another);
		} else if (one instanceof Object[] && another instanceof Object[]) {
			return Arrays.equals((Object[]) one, (Object[]) another);
		} else if (another.getClass() != clazz) {
			return false;
		} else if (clazz == byte[].class) {
			return Arrays.equals((byte[]) one, (byte[]) another);
		} else if (clazz == char[].class) {
			return Arrays.equals((char[]) one, (char[]) another);
		} else if (clazz == double[].class) {
			return Arrays.equals((double[]) one, (double[]) another);
		} else if (clazz == float[].class) {
			return Arrays.equals((float[]) one, (float[]) another);
		} else if (clazz == int[].class) {
			return Arrays.equals((int[]) one, (int[]) another);
		} else if (clazz == long[].class) {
			return Arrays.equals((long[]) one, (long[]) another);
		} else if (clazz == short[].class) {
			return Arrays.equals((short[]) one, (short[]) another);
		} else {
			assert clazz == boolean[].class;
			return Arrays.equals((boolean[]) one, (boolean[]) another);
		}
	}

	private Method[] getMemberMethods() {
		if (memberMethods == null) {
			memberMethods = AccessController.doPrivileged((PrivilegedAction<Method[]>) () -> {
				Method[] methods = type.getDeclaredMethods();
				AccessibleObject.setAccessible(methods, true);
				return methods;
			});
		}
		return memberMethods;
	}

	private int hashCodeImpl() {
		int hash = 0;

		for (Entry<String, Object> entry : memberValues.entrySet()) {
			hash += 127 * entry.getKey().hashCode() ^ memberValueHashCode(entry.getValue());
		}

		return hash;
	}

	private static int memberValueHashCode(Object array) {
		Class clazz = array.getClass();
		if (!clazz.isArray()) {
			return array.hashCode();
		} else if (clazz == byte[].class) {
			return Arrays.hashCode((byte[]) array);
		} else if (clazz == char[].class) {
			return Arrays.hashCode((char[]) array);
		} else if (clazz == double[].class) {
			return Arrays.hashCode((double[]) array);
		} else if (clazz == float[].class) {
			return Arrays.hashCode((float[]) array);
		} else if (clazz == int[].class) {
			return Arrays.hashCode((int[]) array);
		} else if (clazz == long[].class) {
			return Arrays.hashCode((long[]) array);
		} else if (clazz == short[].class) {
			return Arrays.hashCode((short[]) array);
		} else {
			return clazz == boolean[].class ? Arrays.hashCode((boolean[]) array) : Arrays.hashCode((Object[]) array);
		}
	}
}
