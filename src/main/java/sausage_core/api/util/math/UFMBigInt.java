package sausage_core.api.util.math;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.NBTTagLongArray;
import sausage_core.api.util.nbt.NBTs;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Unsigned, Fixed-sized, Mutable implementation of big integers.
 * Only simple operations like add and sub supported
 * for higher efficiency to the time and space.
 * Not highest because of the waste of longs' highest bits
 * for security of the algorithm and data structure.
 *
 * @author Yaossg
 */
public class UFMBigInt implements Comparable<UFMBigInt> {
	public static final long BASE = 1L << 62;
	private static final BigInteger BIG_BASE = BigInteger.valueOf(BASE);
	private final long[] data;
	private int used;

	private static void overflow() {
		throw new ArithmeticException("integer overflow");
	}

	private UFMBigInt(int size) {
		Preconditions.checkArgument(size > 0);
		data = new long[size];
		used = 1;
	}

	private UFMBigInt(UFMBigInt other) {
		data = other.data.clone();
		used = other.used;
	}

	private UFMBigInt(int size, UFMBigInt other) {
		Preconditions.checkArgument(size > 0);
		if (size < other.used) overflow();
		data = Arrays.copyOf(other.data, size);
		used = other.used;
	}

	private UFMBigInt(long[] longs) {
		data = longs.clone();
		used = longs.length;
		shrinkUsed();
	}

	// create new big int

	public static UFMBigInt zeroOf(int size) {
		return new UFMBigInt(size);
	}

	public static UFMBigInt of(int size, long value) {
		UFMBigInt bigInt = new UFMBigInt(size);
		bigInt.assign(value);
		return bigInt;
	}

	public static UFMBigInt of(int size, String value) {
		return of(size, value, 10);
	}

	public static UFMBigInt of(int size, String value, int radix) {
		return of(size, new BigInteger(value, radix));
	}

	public static UFMBigInt of(int size, BigInteger value) {
		UFMBigInt bigInt = new UFMBigInt(size);
		bigInt.assign(value);
		return bigInt;
	}

	public static UFMBigInt maxOf(int size) {
		UFMBigInt bigInt = new UFMBigInt(size);
		bigInt.assignMax();
		return bigInt;
	}

	public UFMBigInt max() {
		return maxOf(data.length);
	}


	public static UFMBigInt fromLongs(long[] longs) {
		return new UFMBigInt(longs);
	}

	// assign new value

	public void assignZero() {
		Arrays.fill(data, 0);
		used = 1;
	}

	public void assign(long value) {
		Preconditions.checkArgument(value >= 0);
		assignZero();
		data[0] = value;
		carryUp();
	}

	public void assign(String value) {
		assign(value, 10);
	}

	public void assign(String value, int radix) {
		assign(new BigInteger(value, radix));
	}

	public void assign(UFMBigInt value) {
		if (data.length < value.used) overflow();
		assignZero();
		System.arraycopy(value.data, 0, data, 0, value.used);
	}

	public void assign(BigInteger value) {
		Preconditions.checkArgument(value.signum() >= 0);
		assignZero();
		if (value.signum() != 0) {
			try {
				used = 0;
				while (value.signum() != 0) {
					BigInteger[] dr = value.divideAndRemainder(BIG_BASE);
					value = dr[0];
					data[++used - 1] = dr[1].longValue();
				}
			} catch (IndexOutOfBoundsException e) {
				overflow();
			}
		}
	}

	public void assignMax() {
		assign(BigInteger.ONE.shiftLeft(62 * data.length).subtract(BigInteger.ONE));
	}

	private void carryUp() {
		for (int i = 0; i < used; ++i) {
			if (data[i] >= BASE) {
				if (used == data.length) overflow();
				data[i] -= BASE;
				++data[i + 1];
			}
		}
		if (used < data.length && data[used] != 0) ++used;
	}

	private void carryDown() {
		for (int i = 0; i < used; ++i)
			if (data[i] < 0) {
				data[i] += BASE;
				--data[i + 1];
			}
		shrinkUsed();
	}

	private void shrinkUsed() {
		while (data[--used] == 0)
			if (used == 0) break;
		++used;
	}

	// addition & subtraction operations

	public UFMBigInt add(UFMBigInt other) {
		if (this == other) return add(other.copy());
		if (other.used > data.length) overflow();
		for (int i = 0; i < used; ++i)
			data[i] += other.data[i];
		carryUp();
		return this;
	}

	public UFMBigInt add(long other) {
		if (other < 0) return sub(-other);
		if (other == 0) return this;
		data[0] += other % BASE;
		if (other >= BASE)
			data[1] += other / BASE;
		carryUp();
		return this;
	}

	public UFMBigInt sub(UFMBigInt other) {
		int compare = compareTo(other);
		Preconditions.checkArgument(compare >= 0);
		if (compare == 0) {
			assignZero();
		} else {
			for (int i = 0; i < other.used; ++i)
				data[i] -= other.data[i];
			carryDown();
		}
		return this;
	}

	public UFMBigInt sub(long other) {
		if (other < 0) return add(-other);
		if (data[0] >= other) {
			data[0] -= other;
		} else {
			data[0] -= other % BASE;
			if (other >= BASE)
				data[1] -= other / BASE;
			carryDown();
		}
		return this;
	}

	public boolean tryAdd(long other) {
		int L = other < BASE ? 1 : 2;
		if (L < data.length && used < data.length) {
			add(other);
			return true;
		}
		UFMBigInt copy = copy();
		try {
			copy.add(other);
		} catch (Exception e) {
			return false;
		}
		assign(copy);
		return true;
	}

	public boolean tryAdd(long other, UFMBigInt limit) {
		UFMBigInt copy = copy();
		if (copy.compareTo(limit) > 0) return false;
		if (copy.tryAdd(other)) {
			if (copy.compareTo(limit) <= 0) {
				assign(copy);
				return true;
			}
		}
		return false;
	}

	public boolean tryAdd(UFMBigInt other) {
		if (other.used < data.length && used < data.length) {
			add(other);
			return true;
		}
		UFMBigInt copy = copy();
		try {
			copy.add(other);
		} catch (Exception e) {
			return false;
		}
		assign(copy);
		return true;
	}

	public boolean tryAdd(UFMBigInt other, UFMBigInt limit) {
		UFMBigInt copy = copy();
		if (copy.compareTo(limit) > 0) return false;
		if (copy.tryAdd(other)) {
			if (copy.compareTo(limit) <= 0) {
				assign(copy);
				return true;
			}
		}
		return false;
	}

	public boolean trySub(long other) {
		int L = other < BASE ? 1 : 2;
		if (L < used) {
			sub(other);
			return true;
		}
		try {
			if (asLong() < other) return false;
			sub(other);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean trySub(UFMBigInt other) {
		int compare = compareTo(other);
		if (compare < 0) {
			return false;
		} else {
			sub(other);
			return true;
		}
	}

	// fill & drain
	// fill: add as more as possible
	// drain: sub as more as possible

	public long fill(long other) {
		if (tryAdd(other)) {
			other = 0;
		} else {
			other -= BASE - 1 - data[0];
			assignMax();
		}
		return other;
	}

	public long fill(long other, UFMBigInt limit) {
		if (tryAdd(other, limit)) {
			other = 0;
		} else {
			other -= limit.copy().sub(this).asLong();
			assign(limit);
		}
		return other;
	}

	public UFMBigInt fill(UFMBigInt other) {
		if (tryAdd(other)) {
			other.assignZero();
		} else {
			other.sub(max().sub(this));
			assignMax();
		}
		return other;
	}

	public UFMBigInt fill(UFMBigInt other, UFMBigInt limit) {
		if (tryAdd(other, limit)) {
			other.assignZero();
		} else {
			other.sub(limit.copy().sub(this));
			assign(limit);
		}
		return other;
	}

	public long drain(long other) {
		if (trySub(other)) {
			return other;
		} else {
			long value = asLong();
			assignZero();
			return value;
		}
	}

	public UFMBigInt drain(UFMBigInt other) {
		if (trySub(other)) {
			return other.copy();
		} else {
			UFMBigInt copy = copy();
			assignZero();
			return copy;
		}
	}

	public BigInteger toBigInt() {
		BigInteger ret = BigInteger.ZERO;
		for (int i = used - 1; i >= 0; --i) {
			ret = ret.multiply(BIG_BASE);
			ret = ret.add(BigInteger.valueOf(data[i]));
		}
		return ret;
	}

	// copy

	public UFMBigInt copy() {
		return new UFMBigInt(this);
	}

	public UFMBigInt copy(int size) {
		return new UFMBigInt(size, this);
	}

	// convert to other data types

	@Override
	public String toString() {
		return toString(10);
	}

	public String toString(int radix) {
		return toBigInt().toString(radix);
	}

	public String toStringGroup() {
		return toString().replaceAll("(?<=\\d)(?=(?:\\d{3})+$)", ",");
	}

	public long asLong() {
		long datum = data[0];
		switch (used) {
			case 2:
				if (data[1] >> 1 != 0) {
					datum |= (data[1] & 1) << 62;
					break;
				}
			default:
				overflow();
			case 1:
		}
		return datum;
	}

	public long[] toLongs() {
		return data.clone();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(Arrays.copyOf(data, used));
	}

	// compare

	@Override
	public int compareTo(UFMBigInt other) {
		if (this == other) return 0;
		if (used != other.used)
			return Integer.compare(used, other.used);
		for (int i = used - 1; i >= 0; --i)
			if (data[i] != other.data[i])
				return Long.compare(data[i], other.data[i]);
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (getClass() != obj.getClass()) return false;
		return compareTo(((UFMBigInt) obj)) == 0;
	}

	// Minecraft Development Begin

	public NBTTagLongArray toNBT() {
		return NBTs.of(toLongs());
	}

	public static UFMBigInt fromNBT(NBTTagLongArray nbt) {
		return new UFMBigInt(nbt.data);
	}

}
