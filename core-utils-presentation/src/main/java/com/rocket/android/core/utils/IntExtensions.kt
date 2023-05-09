/**
 * Returns the absolute value of this integer.
 */
fun Int.abs(): Int = kotlin.math.abs(this)

/**
 * Returns the minimum value between this integer and [other].
 */
fun Int.min(other: Int): Int = kotlin.math.min(this, other)

/**
 * Returns the maximum value between this integer and [other].
 */
fun Int.max(other: Int): Int = kotlin.math.max(this, other)

/**
 * Checks whether this integer is even.
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Checks whether this integer is odd.
 */
fun Int.isOdd(): Boolean = this % 2 != 0

/**
 * Converts this integer to a boolean value, where `0` is `false` and any other value is `true`.
 */
fun Int.toBoolean(): Boolean = this != 0

/**
 * Converts this integer to a string in binary format (base 2).
 */
fun Int.toBinaryString(): String = Integer.toBinaryString(this)

/**
 * Converts this integer to a string in hexadecimal format (base 16).
 */
fun Int.toHexString(): String = Integer.toHexString(this)

/**
 * Converts this integer to a string in octal format (base 8).
 */
fun Int.toOctalString(): String = Integer.toOctalString(this)
