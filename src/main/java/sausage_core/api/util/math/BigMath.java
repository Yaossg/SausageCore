package sausage_core.api.util.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Brainless utils for {@link BigInteger} and {@link BigDecimal}
 *
 * @author Yaossg
 * */
public class BigMath {

	public static BigInteger multiply(BigInteger a, long b) {
		return a.multiply(BigInteger.valueOf(b));
	}

	public static BigDecimal multiply(BigInteger a, double b) {
		return multiply(new BigDecimal(a), b);
	}

	public static BigDecimal multiply(BigDecimal a, long b) {
		return a.multiply(new BigDecimal(b));
	}

	public static BigDecimal multiply(BigDecimal a, double b) {
		return a.multiply(new BigDecimal(b));
	}

	public static BigDecimal multiply(BigDecimal a, BigInteger b) {
		return a.multiply(new BigDecimal(b));
	}

	public static BigDecimal divide(BigInteger a, long b, int scale, RoundingMode roundingMode) {
		return divide(a, BigInteger.valueOf(b), scale, roundingMode);
	}

	public static BigDecimal divide(BigInteger a, double b, int scale, RoundingMode roundingMode) {
		return divide(new BigDecimal(a), b, scale, roundingMode);
	}

	public static BigDecimal divide(BigInteger a, BigInteger b, int scale, RoundingMode roundingMode) {
		return divide(new BigDecimal(a), b, scale, roundingMode);
	}

	public static BigDecimal divide(BigDecimal a, long b, int scale, RoundingMode roundingMode) {
		return divide(a, BigInteger.valueOf(b), scale, roundingMode);
	}

	public static BigDecimal divide(BigDecimal a, double b, int scale, RoundingMode roundingMode) {
		return a.divide(new BigDecimal(b), scale, roundingMode);
	}

	public static BigDecimal divide(BigDecimal a, BigInteger b, int scale, RoundingMode roundingMode) {
		return a.divide(new BigDecimal(b), scale, roundingMode);
	}
}
