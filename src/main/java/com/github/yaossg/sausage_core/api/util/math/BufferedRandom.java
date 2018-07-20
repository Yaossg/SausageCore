package com.github.yaossg.sausage_core.api.util.math;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * byte-saving RNG
 * */
public class BufferedRandom extends Random {
    private Random random; //instance to delegate
    private int buffer = 0;
    private int left = 0;
    private boolean large = false;
    private int[] largeBuffer;
    private int largeLeft = 0;
    public final int largeSize = 0xffffff;
    public static BufferedRandom boxed(Random random) {
        return random instanceof BufferedRandom ? (BufferedRandom) random : new BufferedRandom(random);
    }

    public BufferedRandom() {
        random = this; //actually redundant
    }

    public BufferedRandom(Random random) {
        super(0);
        this.random = random;
    }

    @Override
    public synchronized void setSeed(long seed) {
        if(seed != 0) {
            if(random == null) {
                random = this; // random has not been initialized here
                super.setSeed(seed);
            } else
                random.setSeed(seed);
        }
        hasNextG = false;
    }

    public BufferedRandom setLarge(boolean large) {
        this.large = large;
        largeBuffer = large ? new int[largeSize] : null;
        return this;
    }

    protected void allocate() {
        if(large) {
            if(largeLeft > 0)
                --largeLeft;
            else for (int i = 0; i < (largeLeft = largeSize); i++)
                largeBuffer[i] = nextInt();
            buffer = largeBuffer[largeLeft - 1];
        } else
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
        if(bound > 0x3fffffff)
            return random != this ? random.nextInt(bound) : super.nextInt(bound);
        return nextIntInternal(bound, 0);
    }

    private int nextIntInternal(int bound, int times) {
        int p2 = MathHelper.smallestEncompassingPowerOfTwo(bound);
        int bits = MathHelper.log2(p2);
        int r = next(bits);
        if(r >= bound) {
            if(times > 4) {
                //It is not but almost a random number with the bound
                return nextInt(p2 / 2);
            }
            //recurrences [0.5, 1.0) times on average
            //left case is nextInt(3), right case is impossible actually
            r = nextIntInternal(bound, times + 1); // why not try again?
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
        return (((long) (next(bits)) << Math.max(0, bits - 26)) + next(Math.max(0, bits - 26))) / (1L << bits);
    }

    private boolean hasNextG = false;
    private float nextG;

    public synchronized float nextfloatGaussian() {
        if(hasNextG) {
            hasNextG = false;
            return nextG;
        } else {
            float v1, v2, s;
            do {
                v1 = 2 * nextFloat() - 1; // between -1 and 1
                v2 = 2 * nextFloat() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            float multiplier = MathHelper.sqrt(-2 * StrictMath.log(s) / s);
            nextG = v2 * multiplier;
            hasNextG = true;
            return v1 * multiplier;
        }
    }
}
