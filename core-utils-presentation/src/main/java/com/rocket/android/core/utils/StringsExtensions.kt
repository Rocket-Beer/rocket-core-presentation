package com.rocket.android.core.utils

import android.util.Patterns
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import java.util.regex.Pattern

/**
String Extensions
This Kotlin class provides useful extension functions for working with strings.

Functions
capitalizeFirstLetter()
Capitalizes the first letter of a string.

capitalize()
Capitalizes all letters in a string.

removeLineBreaks()
Removes all line breaks from a string.

toAndroidVersionNumber()
Converts a string representation of an Android version number (e.g., "10.2.3") to an integer.

chunked(chunkSize: Int)
Splits a string into chunks of the specified size. The last chunk may be smaller than the specified size.

replaceIgnoreCase(old: String, new: String)
Replaces all occurrences of a string with another string, ignoring case.

substringSafe(startIndex: Int, endIndex: Int)
Returns a substring of a string starting at the specified index and going up to, but not including, the end index. If the end index is greater than the length of the string, it will be truncated to the end of the string.

formatAsCurrency(locale: Locale, currencyCode: String)
Formats a string as a currency value using the specified locale and currency code.

toBooleanOrNull()
Converts a string to a Boolean value or returns null if the string cannot be converted. Returns true if the string is "true", false if the string is "false", or null otherwise.

toIntegerOrNull()
Converts a string to an Integer value or returns null if the string cannot be converted.

isValidEmail()
Validates if a string is a valid email address using a regular expression pattern.

isValidPhoneNumber()
Validates if a string is a valid phone number using a regular expression pattern.

isValidUrl()
Validates if a string is a valid URL using a regular expression pattern.

isValidDate()
Validates if a string is a valid date in the format "yyyy-MM-dd" using a regular expression pattern.*/

/**
 * Capitalizes the first letter of this string.
 */
fun String.capitalizeFirstLetter(): String =
    this.substring(0, 1).toUpperCase(Locale.getDefault()) + this.substring(1)
        .toLowerCase(Locale.getDefault())

/**
 * Capitalizes this string.
 */
fun String.capitalize(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

/**
 * Removes all line breaks from this string.
 */
fun String.removeLineBreaks(): String =
    replace("\r\n", "").replace("\n", "")

/**
 * Converts this string to an Android version number.
 */
fun String.toAndroidVersionNumber(): Int =
    replace(".", "").toInt()

/**
 * Splits this string into chunks of the specified size.
 * The last chunk may be smaller than the specified size.
 *
 * @param chunkSize The size of each chunk.
 * @return A list of string chunks.
 */
fun String.chunked(chunkSize: Int): List<CharSequence> =
    chunked(chunkSize) { it }

/**
 * Replaces all occurrences of a string with another string, ignoring case.
 *
 * @param old The old string to replace.
 * @param new The new string to replace it with.
 * @return The string with all occurrences of the old string replaced with the new string.
 */
fun String.replaceIgnoreCase(old: String, new: String): String =
    "(?i)$old".toRegex().replace(this, new)

/**
 * Returns a substring of this string starting at the specified index and going up to, but not including,
 * the end index. If the end index is greater than the length of the string, it will be truncated to the
 * end of the string.
 *
 * @param startIndex The starting index of the substring.
 * @param endIndex The ending index of the substring (exclusive).
 * @return The substring.
 */
fun String.substringSafe(startIndex: Int, endIndex: Int): String {
    val start = if (startIndex < 0) 0 else startIndex
    val end = if (endIndex > length) length else endIndex
    return substring(start, end)
}

/**
 * Formats this string as a currency value using the specified locale and currency code.
 *
 * @param locale The locale to use.
 * @param currencyCode The ISO 4217 currency code to use.
 * @return The currency-formatted string.
 */
fun String.formatAsCurrency(locale: Locale, currencyCode: String): String {
    val currency = Currency.getInstance(currencyCode)
    val format = NumberFormat.getCurrencyInstance(locale)
    format.currency = currency
    return format.format(toDoubleOrNull() ?: 0.0)
}

/**
 * Converts the given string to a Boolean value or returns null if the string cannot be converted.
 * @return `true` if the string is "true", `false` if the string is "false", or `null` otherwise.
 */
fun String?.toBooleanOrNull(): Boolean? {
    if (this == null) return null
    return when (this.lowercase(Locale.ROOT)) {
        "true" -> true
        "false" -> false
        else -> null
    }
}

/**
 * Converts the given string to an Integer value or returns null if the string cannot be converted.
 * @return The Integer value of the
string or null if it cannot be converted.
 */
fun String?.toIntegerOrNull(): Int? {
    return try {
        this?.toInt()
    } catch (e: NumberFormatException) {
        null
    }
}

/**
 * Validates if this string is a valid email address using a regular expression pattern.
 * @return true if the string is a valid email address, false otherwise.
 */

fun String.isValidEmail(): Boolean {
    val pattern = Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +
            "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
            "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.)|" +
            "(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    )
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

/**
 * Validates if this string is a valid phone number using a regular expression pattern.
 * @return true if the string is a valid phone number, false otherwise.
 */
fun String.isValidPhoneNumber(): Boolean {
    val pattern = Pattern.compile(
        "^[+]?[0-9]{10,13}$"
    )
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

/**
 * Validates if this string is a valid URL using a regular expression pattern.
 *@return true if the string is a valid URL, false otherwise.
 */
fun String.isValidUrl(): Boolean {
    val pattern = Patterns.WEB_URL
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

/**

Validates if this string is a valid date in the format yyyy-MM-dd using a regular expression pattern.
@return true if the string is a valid date, false otherwise.
 */
fun String.isValidDate(): Boolean {
    val pattern = Pattern.compile(
        "^(?:(?:19|20)[0-9]{2})-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-" +
            "(?:29|30)|(?:0[13578]|1[02])-31)$"
    )
    val matcher = pattern.matcher(this)
    return matcher.matches()
}
