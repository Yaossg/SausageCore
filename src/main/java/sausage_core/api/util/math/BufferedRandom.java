package sausage_core.api.util.math;

import com.google.common.math.IntMath;

import java.math.RoundingMode;
import java.util.Random;

/**
 * byte-saving and {@link java.util.Random} supported RNG
 */
public class BufferedRandom extends Random {
	protected Random random; //instance to delegate
	protected int buffer = 0;
	protected int left = 0;
	private static final BufferedRandom INSTANCE = new BufferedRandom();

	public static BufferedRandom shared() {
		return INSTANCE;
	}

	public static BufferedRandom provide() {
		return new BufferedRandom();
	}

	public static BufferedRandom provide(long seed) {
		BufferedRandom random = new BufferedRandom();
		random.setSeed(seed);
		return random;
	}

	public static BufferedRandom boxed(Random random) {
		return random instanceof BufferedRandom ? (BufferedRandom) random : new BufferedRandom(random);
	}

	private BufferedRandom() {}

	private BufferedRandom(Random random) {
		super(0);
		this.random = random;
	}

	@Override
	public synchronized void setSeed(long seed) {
		if(seed != 0) {
			if(random == null) {
				random = this;
				super.setSeed(seed);
			} else
				random.setSeed(seed);
		}
		hasNextG = false;
	}

	protected void allocate() {
		buffer = nextInt();
		left = 32;
	}

	@Override
	public int next(int bits) {
		if(bits == 0)
			return bits;
		if(left == 0)
			allocate();
		int ret = 0;
		if(bits > left) {
			bits -= left;
			ret = buffer << bits;
			allocate();
		}
		ret |= buffer & ~(-1 << bits);
		left -= bits;
		buffer >>>= bits;
		return ret;
	}

	@Override
	public int nextInt() {
		return random != this ? random.nextInt() : super.next(32);
	}

	@Override
	public int nextInt(int bound) {
		if(bound <= 0)
			throw new IllegalArgumentException("bound must be positive");
		if(bound == 1)
			return 0;
		if(bound == 2)
			return next(1);
		return nextIntInternal(bound);
	}

	private int nextIntInternal(int bound) {
		int p2 = IntMath.ceilingPowerOfTwo(bound);
		int bits = IntMath.log2(p2, RoundingMode.UNNECESSARY);
		int r = next(bits);
		if(r >= bound) {
			int r2 = next(bits) + r - bound;
			r = (int) ((bound / (2.0f * p2 - bound)) * r2);
		}
		return r;
	}

	@Override
	public long nextLong() {
		return random != this ? random.nextLong() : ((long) (super.next(32)) << 32) + super.next(32);
	}

	public float nextFloat(int bits) {
		return next(bits) / ((float) (1 << bits));
	}

	public double nextDouble(int bits) {
		return (((long) (next(bits)) << Math.max(0, bits - 26)) + next(Math.max(0, bits - 26))) / (double) (1L << bits);
	}

	private boolean hasNextG = false;
	private float nextG;

	public synchronized float nextFloatGaussian() {
		if(hasNextG) {
			hasNextG = false;
			return nextG;
		} else {
			float v1, v2, s;
			do {
				v1 = 2 * nextFloat() - 1; // between -1 and 1
				v2 = 2 * nextFloat() - 1; // between -1 and 1
				s = v1 * v1 + v2 * v2;
			} while(s >= 1 || s == 0);
			float multiplier = (float) Math.sqrt(-2 * StrictMath.log(s) / s);
			nextG = v2 * multiplier;
			hasNextG = true;
			return v1 * multiplier;
		}
	}
}
