package com.rocket.android.core.utils

/**
 *
# Integer Extensions

This extension class provides a collection of useful functions for working with Integers in Android projects. These functions can be used to perform common operations, conversions, and validations on Integers.

## Functions

- `absoluteValue()`: Returns the absolute value of the integer.
- `abs()`: Returns the absolute value of this integer.
- `min(other: Int)`: Returns the minimum value between this integer and the specified integer.
- `max(other: Int)`: Returns the maximum value between this integer and the specified integer.
- `toBoolean()`: Converts this integer to a boolean value, where `0` is `false` and any other value is `true`.
- `toBinaryString()`: Converts this integer to a string in binary format (base 2).
- `toHexString()`: Converts this integer to a string in hexadecimal format (base 16).
- `toOctalString()`: Converts this integer to a string in octal format (base 8).

## Usage

To use these extensions, simply import the extension class into your Android project and call the desired function on an Integer object. For example:

```kotlin
val number = 42
val absolute = number.absoluteValue()
val isPositive = number.toBoolean()
val binaryString = number.toBinaryString()
 */

/**
 * Extension functions for the Int class.
 */

/**
 * Returns the absolute value of this integer.
 *
 * @return The absolute value of the integer.
 */
fun Int.abs(): Int = kotlin.math.abs(this)

/**
 * Returns the minimum value between this integer and [other].
 *
 * @param other The other integer to compare.
 * @return The minimum value between this integer and [other].
 */
fun Int.min(other: Int): Int = kotlin.math.min(this, other)

/**
 * Returns the maximum value between this integer and [other].
 *
 * @param other The other integer to compare.
 * @return The maximum value between this integer and [other].
 */
fun Int.max(other: Int): Int = kotlin.math.max(this, other)

/**
 * Converts this integer to a boolean value, where `0` is `false` and any other value is `true`.
 *
 * @return The boolean value corresponding to this integer.
 */
fun Int.toBoolean(): Boolean = this != 0

/**
 * Converts this integer to a string in binary format (base 2).
 *
 * @return The binary string representation of this integer.
 */
fun Int.toBinaryString(): String = Integer.toBinaryString(this)

/**
 * Converts this integer to a string in hexadecimal format (base 16).
 *
 * @return The hexadecimal string representation of this integer.
 */
fun Int.toHexString(): String = Integer.toHexString(this)

/**
 * Converts this integer to a string in octal format (base 8).
 *
 * @return The octal string representation of this integer.
 */
fun Int.toOctalString(): String = Integer.toOctalString(this)

/**
 * Converts this integer to a string in a specified radix.
 *
 * @param radix The radix to use for the conversion (2 to 36).
 * @return The string representation of this integer in the specified radix.
 * @throws IllegalArgumentException if the radix is not within the valid range.
 */
fun Int.toString(radix: Int): String {
    require(radix in 2..36) { "Radix must be between 2 and 36." }
    return Integer.toString(this, radix)
}

/**
 * Checks if this integer is even.
 *
 * @return `true` if this integer is even, `false` otherwise.
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Checks if this integer is odd.
 *
 * @return `true` if this integer is odd, `false` otherwise.
 */
fun Int.isOdd(): Boolean = this % 2 != 0

/**
 * Rounds this integer to the nearest multiple of [step].
 *
 * @param step The step value for rounding.
 * @return The nearest multiple of [step] to this integer.
 */
fun Int.roundToNearest(step: Int): Int {
    require(step != 0) { "Step must not be zero." }
    val remainder = this % step
    return when {
        remainder >= step / 2 -> this + step - remainder
        remainder < -step / 2 -> this - step - remainder
        else -> this - remainder
    }
}

/**
 * Performs the specified action on each element in the range of this integer.
 *
 * @param action The action to perform on each element.
 */
inline fun Int.forEach(action: (Int) -> Unit) {
    for (i in 0 until this) {
        action(i)
    }
}
