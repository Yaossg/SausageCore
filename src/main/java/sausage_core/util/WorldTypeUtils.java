package sausage_core.util;

import com.google.common.primitives.Ints;

public class WorldTypeUtils {
    public static int parseInt(String string) {
        Integer integer = Ints.tryParse(string);
        return integer == null ? 0 : integer;
    }

    public static long chunkID(int x, int z) {
        return (long)x & 0xffffffffL | ((long)z & 0xffffffffL) << 32;
    }

    public static long chunkID_pos(int x, int z) {
        return chunkID(x >> 4, z >> 4);
    }

    public static long chunkID_offset(int x, int z) {
        return chunkID_pos(x - 2, z - 2);
    }

    public static int chunkX(long chunkID) {
        return (int) (chunkID & 0xffffffffL);
    }

    public static int chunkZ(long chunkID) {
        return (int) (chunkID >>> 32 & 0xffffffffL);
    }

    public static int hashChunk(int x, int z) {
        int i = 1664525 * x + 1013904223;
        int j = 1664525 * (z ^ -559038737) + 1013904223;
        return i ^ j;
    }

    public static int hashChunk(long chunkID) {
        return hashChunk(chunkX(chunkID), chunkZ(chunkID));
    }

    public static long scale(long chunkID, int k) {
        int x = chunkX(chunkID);
        int z = chunkZ(chunkID);
        x >>>= k;
        z >>>= k;
        return chunkID(x, z);
    }
}
