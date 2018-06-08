package com.github.yaossg.sausage_core.api.util;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class BufferedRandom extends Random { // provide interface
    private Random random = this; //instance to delegate
    private int buffer = 0;
    private int left = 0;

    public static BufferedRandom boxed(Random random) {
        return random instanceof BufferedRandom ? (BufferedRandom) random : new BufferedRandom(random);
    }

    public BufferedRandom() { }

    public BufferedRandom(Random random) {
        super(0);
        this.random = random;
    }

    @Override
    public synchronized void setSeed(long seed) {
        if(seed != 0) {
            if(random == null) {
                random = this;
                super.setSeed(seed);
            }
            else
                random.setSeed(seed);
        }
        haveGaussianFloat = false;
    }

    protected void allocate() {
        buffer = random.nextInt();
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
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");
        if(bound == 1)
            return 0;
        if(bound == 2)
            return next(1);
        int p2 = MathHelper.smallestEncompassingPowerOfTwo(bound);
        int bits = MathHelper.log2(p2);
        int r = next(bits);
        if(r >= bound) {
            float f = (float)bound / (p2 - bound);
            r %= bound;
            r *= f;
            r %= bound;
        }
        return r;
    }

    @Override
    public long nextLong() {
        return random != this ? random.nextLong() : ((long)(super.next(32)) << 32) + super.next(32);
    }

    public float nextFloat(int bits) {
        return next(bits) / ((float)(1 << bits));
    }

    public double nextDouble(int bits) {
        return  (((long)(next(bits)) << Math.max(0, bits - 26)) + next(Math.max(0, bits - 26))) / (1L << bits);
    }

    private boolean haveGaussianFloat = false;
    private float nextGaussianFloat;

    public synchronized float nextGaussianFloat() {
        if (haveGaussianFloat) {
            haveGaussianFloat = false;
            return nextGaussianFloat;
        } else {
            float v1, v2, s;
            do {
                v1 = 2 * nextFloat() - 1; // between -1 and 1
                v2 = 2 * nextFloat() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            float multiplier = MathHelper.sqrt(-2 * StrictMath.log(s) / s);
            nextGaussianFloat = v2 * multiplier;
            haveGaussianFloat = true;
            return v1 * multiplier;
        }
    }
}
