import android.os.Build
import java.io.File

/**
 * This function is used to check if an application is rooted.
 */
fun isRoot(): Boolean {
    val binaryName = "su"
    val places = arrayOf(
        "/sbin/", "/system/bin/", "/system/xbin/", " /data/local/xbin/",
        "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"
    )

    places.forEach {
        if (File(it + binaryName).exists()) return true
    }
    return false
}

/**
 * This function is used to check whether an application is running on an emulator.
 */

fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.BRAND.startsWith("generic")
            && Build.DEVICE.startsWith("generic")
            || "google_sdk" == Build.PRODUCT)
}