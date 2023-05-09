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
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}

/**
 * Changes the color of the status bar and the color of the icons (if whiteIcons is true) in an Activity.
 *
 * @param idColor the color resource id to use for the status bar
 * @param whiteIcons true if the icons should be white, false otherwise
 */
fun Context.changeStatusBarColor(idColor: Int, whiteIcons: Boolean) {
    if (whiteIcons)
        (this as? Activity)?.window?.peekDecorView()?.systemUiVisibility = 0
    else
        (this as? Activity)?.window?.peekDecorView()?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    (this as? Activity)?.window?.statusBarColor = ContextCompat.getColor(this, idColor)
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
    return ColorStateList.valueOf(
        ContextCompat.getColor(this, colorId)
    )
}

/**
 * Returns a drawable object associated with a particular resource ID.
 *
 * @param resourceId The resource ID of the desired drawable.
 * @return A Drawable object if the resource is found, or null otherwise.
 */
fun Context.getDrawableById(resourceId: Int?): Drawable? {
    return try {
        if (resourceId != null && resourceId != 0) {
            ContextCompat.getDrawable(this, resourceId)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Returns the resource ID of a resource with a given name and type.
 *
 * @param name The name of the desired resource.
 * @param type The type of the desired resource (e.g. "drawable", "string", "color", etc.).
 * @return The resource ID of the desired resource, or 0 if the resource is not found.
 */
fun Context.getResourceByName(name: String, type: String): Int {
    // Use the resources.getIdentifier() method to get the resource ID of the resource with the given name and type.
    // Pass in the package name of the application to ensure that the method looks for the resource in the correct package.
    return this.resources.getIdentifier(name, type, packageName)
}

/**
 * This function hides the soft keyboard associated with the provided view.
 *
 * @param view The view that has focus and is displaying the soft keyboard.
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Shows the soft keyboard.
 */
fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Returns the color with the given resource id.
 */
fun Context.getCompatColor(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}

/**
 * Returns the drawable with the given resource id.
 */
fun Context.getCompatDrawable(@DrawableRes drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableResId)
}

/**
 * Returns the dimension with the given resource id in pixels.
 */
fun Context.getDimensionPixelSize(resourceId: Int): Int {
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * Returns the screen width in pixels.
 */
fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

/**
 * Returns the screen height in pixels.
 */
fun Context.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

/**
 * Returns the themed color with the given attribute id.
 */
fun Context.getThemedColor(attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

/**
 * Inflates a layout resource into a view.
 */
fun Context.inflateLayout(layoutResId: Int): View {
    return LayoutInflater.from(this).inflate(layoutResId, null)
}

/**
 * Opens the settings page for this app.
 */
fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    startActivity(intent)
}

/**
 * Restarts the current activity.
 */
fun Context.restartActivity() {
    val intent = Intent(this, this.javaClass)
    startActivity(intent)
}

/**
 * Starts an activity for a given action.
 */
inline fun <reified T : Any> Context.startActivity(action: String) {
    val intent = Intent(action)
    startActivity(intent)
}

/**
 * Starts an activity for a given class.
 */
inline fun <reified T : Any> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}
