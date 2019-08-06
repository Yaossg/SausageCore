package sausage_core.api.util.reflect;

import com.google.common.primitives.Primitives;

public enum EnumPrimitive {
	// the void type
	VOID   (void.class, 0),
	// the boolean type
	BOOLEAN(boolean.class, 1),
	// integral types
	BYTE   (byte.class, Byte.BYTES),
	CHAR   (char.class, Character.BYTES),
	SHORT  (short.class, Short.BYTES),
	INT    (int.class, Integer.BYTES),
	LONG   (long.class, Long.BYTES),
	// floating-point types
	FLOAT  (float.class, Float.BYTES),
	DOUBLE (double.class, Double.BYTES);

	public final Class<?> type;
	public final int size;

	EnumPrimitive(Class<?> type, int size) {
		this.type = type;
		this.size = size;
	}

	public static EnumPrimitive match(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return valueOf(clazz.getName().toUpperCase());
		}
		throw new IllegalArgumentException();
	}

	public boolean isVoid() {
		return this == VOID;
	}

	public boolean isBoolean() {
		return this == BOOLEAN;
	}

	public boolean isIntegral() {
		switch (this) {
			case VOID: case BOOLEAN: case FLOAT: case DOUBLE: return false;
		}
		return true;
	}

	public boolean isFloatingPoint() {
		switch (this) {
			case FLOAT: case DOUBLE: return true;
		}
		return false;
	}

	public boolean isNumeric() {
		switch (this) {
			case VOID: case BOOLEAN: return false;
		}
		return true;
	}

	public Class<?> wrappedType() {
		return Primitives.wrap(type);
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
