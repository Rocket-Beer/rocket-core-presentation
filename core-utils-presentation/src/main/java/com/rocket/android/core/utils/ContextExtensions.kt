import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


/**
 * # Context Extensions

This is a collection of extension functions for the Context class in Android applications. These extensions provide convenient utility methods to perform common tasks and access resources easily.

## Usage

To use these extensions in your Android project, follow these steps:

1. Copy the desired extension functions into your project's codebase.
2. Make sure to import the necessary dependencies if required.
3. Use the extension functions by calling them on a Context object.

Example:

```kotlin
// Copy text to the clipboard
context.copyToClipboard("label", "Hello, world!")

// Open a browser with a specific URI
val uri = Uri.parse("https://www.example.com")
context.openBrowser(uri)

// Vibrate the device
context.vibratePhone()



// And more...
Extension Functions
The following extension functions are available:

copyToClipboard(label: String = "label", text: CharSequence): Copies the given text to the clipboard with the provided label.

openBrowser(uri: Uri): Opens the browser to the given URI.

vibratePhone(): Vibrates the device for a short duration.


getTintColor(colorId: Int): ColorStateList: Returns a ColorStateList representing a single color.

getDrawableById(resourceId: Int?): Drawable?: Returns a drawable object associated with the provided resource ID.

getResourceByName(name: String, type: String): Int: Returns the resource ID of a resource with a given name and type.

hideKeyboard(view: View): Hides the soft keyboard associated with the provided view.

showKeyboard(view: View): Shows the soft keyboard.

getCompatColor(colorResId: Int): Int: Returns the color with the given resource ID.

getCompatDrawable(drawableResId: Int): Drawable?: Returns the drawable with the given resource ID.

getDimensionPixelSize(resourceId: Int): Int: Returns the dimension with the given resource ID in pixels.

getScreenWidth(): Int: Returns the screen width in pixels.

getScreenHeight(): Int: Returns the screen height in pixels.

getThemedColor(attrId: Int): Int: Returns the themed color with the given attribute ID.

inflateLayout(layoutResId: Int): View: Inflates a layout resource into a view.

openAppSettings(): Opens the settings page for the app.

restartActivity(): Restarts the current activity.

startActivity(action: String): Starts an activity for a given action.

startActivity(): Starts an activity for a given class.
 */

/**
 * Copies the given text to the clipboard with the given label.
 *
 * @param label the label to use for the clipboard item
 * @param text the text to copy to the clipboard
 */
fun Context.copyToClipboard(label: String = "label", text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

/**
 * Opens the browser to the given URI.
 *
 * @param uri the URI to open in the browser
 */
fun Context.openBrowser(uri: Uri) {
    val browserIntent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(browserIntent)
}

/**
 * Vibrates the device for a short duration.
 */
fun Context.vibratePhone() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}


/**
 * Returns a ColorStateList that represents a single color, which can be used to apply a tint to visual
 * elements in an Android application.
 *
 * @param colorId The color resource ID to retrieve.
 *
 * @return A ColorStateList that represents the requested color.
 */
fun Context.getTintColor(@ColorRes colorId: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
}

/**
 * Returns a drawable object associated with a particular resource ID.
 *
 * @param resourceId The resource ID of the desired drawable.
 * @return A Drawable object if the resource is found, or null otherwise.
 */
fun Context.getDrawableById(@DrawableRes resourceId: Int?): Drawable? {
    if (resourceId != null && resourceId != 0) {
        return ContextCompat.getDrawable(this, resourceId)
    }
    return null
}

/**
 * Returns the resource ID of a resource with a given name and type.
 *
 * @param name The name of the desired resource.
 * @param type The type of the desired resource (e.g. "drawable", "string", "color", etc.).
 * @return The resource ID of the desired resource, or
0 if the resource is not found.
 */
fun Context.getResourceByName(name: String, type: String): Int {
    return resources.getIdentifier(name, type, packageName)
}

/**

Hides the soft keyboard associated with the provided view.
@param view The view that has focus and is displaying the soft keyboard.
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
/**

Shows the soft keyboard.
@param view The view that needs to receive the soft keyboard input.
 */
fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}
/**

Returns the color with the given resource ID.
@param colorResId The color resource ID to retrieve.
@return The color value.
 */
fun Context.getCompatColor(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}
/**

Returns the drawable with the given resource ID.
@param drawableResId The drawable resource ID to retrieve.
@return The drawable object.
 */
fun Context.getCompatDrawable(@DrawableRes drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableResId)
}
/**

Returns the dimension with the given resource ID in pixels.
@param resourceId The dimension resource ID to retrieve.
@return The dimension value in pixels.
 */
fun Context.getDimensionPixelSize(resourceId: Int): Int {
    return resources.getDimensionPixelSize(resourceId)
}
/**

Returns the screen width in pixels.
@return The screen width in pixels.
 */
fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}
/**

Returns the screen height in pixels.
@return The screen height in pixels.
 */
fun Context.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}
/**

Returns the themed color with the given attribute ID.
@param attrId The attribute ID of the desired color.
@return The themed color value.
 */
fun Context.getThemedColor(attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}
/**

Inflates a layout resource into a view.
@param layoutResId The layout resource ID to inflate.
@return The inflated view.
 */
fun Context.inflateLayout(layoutResId: Int): View {
    return LayoutInflater.from(this).inflate(layoutResId, null)
}
/**

Opens the settings page for this app.
 */
fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    startActivity(intent)
}
/**

Restarts the current activity.
 */
fun Context.restartActivity() {
    val intent = Intent(this, this.javaClass)
    startActivity(intent)
}
/**

Starts an activity for a given action.
@param action The action to perform.
 */
inline fun <reified T : Any> Context.startActivity(action: String) {
    val intent = Intent(action)
    startActivity(intent)
}
/**

Starts an activity for a given class.
 */
inline fun <reified T : Any> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}
