package sausage_core.api.util.math;

/**
 * Brainless utils for {@link UFMBigInt}
 *
 * @author Yaossg
 * */
public final class UFMBigInts {
	private UFMBigInts() {}

	public UFMBigInt min(UFMBigInt a, UFMBigInt b) {
		return a.compareTo(b) < 0 ? a : b;
	}

	public UFMBigInt max(UFMBigInt a, UFMBigInt b) {
		return a.compareTo(b) > 0 ? a : b;
	}

	public UFMBigInt[] minmax(UFMBigInt a, UFMBigInt b) {
		return a.compareTo(b) < 0 ? new UFMBigInt[] {a, b} : new UFMBigInt[] {b, a};
	}

	public int compare(UFMBigInt a, UFMBigInt b) {
		return a.compareTo(b);
	}

	public UFMBigInt difference(int size, UFMBigInt a, UFMBigInt b) {
		UFMBigInt[] minmax = minmax(a, b);
		return minmax[1].copy(size).sub(minmax[0]);
	}
}
