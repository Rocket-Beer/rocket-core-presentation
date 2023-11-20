import android.content.Context
import android.os.Build
import dalvik.system.DexClassLoader
import java.io.File
import java.util.regex.Pattern

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

/**
 * This function is used to check if an application is in debugging
 */
fun isDownloadedFromStore(playStoreAppId: String, context: Context): Boolean {
    val installer = context.packageManager.getInstallerPackageName(context.packageName)
    return installer != null && installer.startsWith(playStoreAppId)
}

/**
 * This function is used to check whether an application is being handled at runtime
 */
fun checkXposed(context: Context): Boolean {
    try {
        val xposedBridge = File("/system/framework/XposedBridge.jar")
        if (xposedBridge.exists()) {
            val optimizedDir = context.getDir("dex", Context.MODE_PRIVATE)
            val dexClassLoader = DexClassLoader(
                xposedBridge.path,
                optimizedDir.path, null,
                ClassLoader.getSystemClassLoader()
            )
            val xPosedBridge = dexClassLoader.loadClass("de.robv.android.xposed.XposedBridge")
            val getXposedVersion = xPosedBridge.getDeclaredMethod("getXposedVersion")
            if (!getXposedVersion.isAccessible) getXposedVersion.isAccessible = true
            return getXposedVersion.invoke(null) != null
        }
    } catch (ignored: Exception) {
        //NOTHING TO DO HERE
    }
    return false
}

/**
 * This function is used to validate the entered data by using regular expressions
 */
fun isValidEmailString(emailRegex: String, emailString: String): Boolean {
    return emailString.isNotEmpty() && Pattern.compile(emailRegex).matcher(emailString).matches()
}