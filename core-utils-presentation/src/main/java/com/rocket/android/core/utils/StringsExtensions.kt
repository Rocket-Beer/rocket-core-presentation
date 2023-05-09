package com.rocket.android.core.utils

import android.util.Patterns
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import java.util.regex.Pattern

/**
 * Capitalizes the first letter of this string.
 */
fun String.capitalizeFirstLetter(): String =
    this.substring(0, 1).toUpperCase(Locale.getDefault()) + this.substring(1).toLowerCase(
        Locale.getDefault()
    )

/**
 * Removes accents from this CharSequence.
 */
/*fun CharSequence.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}*/

/**
 * Capitalizes this string.
 */
fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

/**
 * Removes all line breaks from this string.
 */
fun String.removeLineBreaks(): String {
    return replace("\r\n", "").replace("\n", "")
}

/**
 * Converts this string to an Android version number.
 */
fun String.toAndroidVersionNumber(): Int = this.replace(".", "").toInt()

/**
 * Splits this string into chunks of the specified size.
 * The last chunk may be smaller than the specified size.
 *
 * @param chunkSize The size of each chunk.
 * @return A list of string chunks.
 */
fun String.chunked(chunkSize: Int): List<CharSequence> = chunked(chunkSize) { it }

/**
 * Replaces all occurrences of a string with another string, ignoring case.
 *
 * @param old The old string to replace.
 * @param new The new string to replace it with.
 * @return The string with all occurrences of the old string replaced with the new string.
 */
fun String.replaceIgnoreCase(old: String, new: String): String {
    return "(?i)$old".toRegex().replace(this, new)
}

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
    return when (this.toLowerCase()) {
        "true" -> true
        "false" -> false
        else -> null
    }
}

/**
 * Converts the given string to an Integer value or returns null if the string cannot be converted.
 * @return The Integer value of the string, or `null` if the string cannot be converted to an Integer.
 */
fun String?.toIntOrNull(): Int? {
    if (this == null) return null
    return try {
        Integer.parseInt(this)
    } catch (e: NumberFormatException) {
        null
    }
}

/**
 * Converts the given string to a Long value or returns null if the string cannot be converted.
 * @return The Long value of the string, or `null` if the string cannot be converted to a Long.
 */
fun String?.toLongOrNull(): Long? {
    if (this == null) return null
    return try {
        java.lang.Long.parseLong(this)
    } catch (e: NumberFormatException) {
        null
    }
}

/**
 * Converts the given string to a Float value or returns null if the string cannot be converted.
 * @return The Float value of the string, or `null` if the string cannot be converted to a Float.
 */
fun String?.toFloatOrNull(): Float? {
    if (this == null) return null
    return try {
        java.lang.Float.parseFloat(this)
    } catch (e: NumberFormatException) {
        null
    }
}

/**
 * Converts the given string to a Double value or returns null if the string cannot be converted.
 * @return The Double value of the string, or `null` if the string cannot be converted to a Double.
 */
fun String?.toDoubleOrNull(): Double? {
    if (this == null) return null
    return try {
        java.lang.Double.parseDouble(this)
    } catch (e: NumberFormatException) {
        null
    }
}

/**
 * Returns a new string with the first character of each word in the original string capitalized.
 * @return The capitalized string.
 */
fun String.capitalizeWords(): String = this.split(" ").joinToString(" ") { it.capitalize() }

/**
 * Returns the first word in the given string or `null` if there is no word.
 * @return The first word in the string, or `null` if the string has no words.
 */
fun String?.firstWordOrNull(): String? {
    if (this == null) return null
    val trimmed = this.trim()
    val firstSpaceIndex = trimmed.indexOf(" ")
    return if (firstSpaceIndex == -1) trimmed else trimmed.substring(0, firstSpaceIndex)
}

/**
 * Truncates the string to a maximum length, appending the specified trailing string if the string
 * is longer than the maximum length. Returns null if the string is null.
 */
fun String?.truncate(maxLength: Int, trailing: String = "..."): String? {
    if (this == null) return null
    if (this.length <= maxLength) return this
    return this.substring(0, maxLength - trailing.length) + trailing
}

// STRING VALIDATIONS

/**
 * Extension function to check if a string is a valid email address.
 *
 * @return true if the string is a valid email address, false otherwise.
 */
fun String.isValidEmail(): Boolean {
    val pattern = Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +
            "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
            "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.)|" +
            "(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    )
    return pattern.matcher(this).matches()
}

/**
 * Extension function to check if a string is a valid phone number.
 *
 * @return true if the string is a valid phone number, false otherwise.
 */
fun String.isValidPhoneNumber(): Boolean {
    val pattern = Pattern.compile("^[+]?[0-9]{10,13}\$")
    return pattern.matcher(this).matches()
}

/**
 * Extension function to check if a string is a valid URL.
 *
 * @return true if the string is a valid URL, false otherwise.
 */
fun String.isValidUrl(): Boolean {
    val pattern = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?\$")
    return pattern.matcher(this).matches()
}

/**
 * Returns true if the string is a valid URL, false otherwise.
 */
fun String?.validateUrl(): Boolean {
    if (this == null) return false
    val regex = "^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ;,./?%&=]*)?$"
    return regex.toRegex().matches(this)
}

/**
 * Extension function to check if a string is a valid date in the format yyyy-MM-dd.
 *
 * @return true if the string is a valid date in the format yyyy-MM-dd, false otherwise.
 */
fun String.isValidDate(): Boolean {
    val pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}\$")
    return pattern.matcher(this).matches()
}

const val DNI_LENGTH = 9
val DNI_LETTER_LIST = arrayOf('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

/**
 * Checks whether the String is a valid NIE (Foreigner Identification Number).
 * @return true if the String is a valid NIE, false otherwise.
 */
fun String.isValidNie(): Boolean {
    var nie = this
    var resultado = false
    if (nie.length == 9 && (
        nie.startsWith("x") || nie.startsWith("X") ||
            nie.startsWith("y") || nie.startsWith("Y") ||
            nie.startsWith("z") || nie.startsWith("Z")
        )
    ) {

        if (nie.startsWith("x") || nie.startsWith("X")) {
            nie = "0" + nie.substring(1)
        } else if (nie.startsWith("y") || nie.startsWith("Y")) {
            nie = "1" + nie.substring(1)
        } else if (nie.startsWith("z") || nie.startsWith("Z")) {
            nie = "2" + nie.substring(1)
        }

        // TODO if (nie.isValidDni())
        if (false) {
            resultado = true
        }
    }
    return resultado
}

/**
 * Checks whether the String is a valid NIS (Social Security Number).
 * @return true if the String is a valid NIS, false otherwise.
 */
fun String.isValidNis(): Boolean {
    var resultado = false
    if (this.length in 9..10) {
        resultado = true

        var index = 0
        while (index < this.length && resultado) {
            if (!Character.isDigit(this[index])) {
                resultado = false
            }
            index++
        }
    }
    return resultado
}

/**
 * Validates a phone number String by checking if it matches the PHONE pattern.
 * @return the validated phone number String if it matches the pattern, an empty String otherwise.
 */
fun String?.validatePhone(): String =
    if (!this.isNullOrEmpty() && Patterns.PHONE.matcher(this).matches())
        this.substring(this.length.minus(9))
    else
        ""

/**
 * Checks whether the String represents a PDF document by comparing its
**/

fun String.validateEmailAddress() = Patterns.EMAIL_ADDRESS.matcher(this).matches()
