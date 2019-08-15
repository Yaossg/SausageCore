// ============================================================================
//   Copyright 2006, 2007, 2008, 2008 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================

package sausage_core.api.util.math;

/**
 * Utility methods for working with binary data.
 *
 * @author Daniel Dyer
 */
public final class BinaryUtils {
	// Mask for casting a byte to an int, bit-by-bit (with
	// bitwise AND) with no special consideration for the sign bit.
	private static final int BITWISE_BYTE_TO_INT = 0x000000FF;

	private BinaryUtils() {}


	/**
	 * Take four bytes from the specified position in the specified
	 * block and convert them into a 32-bit int, using the big-endian
	 * convention.
	 *
	 * @param bytes  The data to read from.
	 * @param offset The position to start reading the 4-byte int from.
	 * @return The 32-bit integer represented by the four bytes.
	 */
	public static int convertBytesToInt(byte[] bytes, int offset) {
		return (BITWISE_BYTE_TO_INT & bytes[offset + 3])
				| ((BITWISE_BYTE_TO_INT & bytes[offset + 2]) << 8)
				| ((BITWISE_BYTE_TO_INT & bytes[offset + 1]) << 16)
				| ((BITWISE_BYTE_TO_INT & bytes[offset]) << 24);
	}


	/**
	 * Convert an array of bytes into an array of ints.  4 bytes from the
	 * input data map to a single int in the output data.
	 *
	 * @param bytes The data to read from.
	 * @return An array of 32-bit integers constructed from the data.
	 */
	public static int[] convertBytesToInts(byte[] bytes) {
		if (bytes.length % 4 != 0) {
			throw new IllegalArgumentException("Number of input bytes must be a multiple of 4.");
		}
		int[] ints = new int[bytes.length / 4];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = convertBytesToInt(bytes, i * 4);
		}
		return ints;
	}
}
