import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.rocket.android.core.security.SecurityConstants
import dalvik.system.DexClassLoader
import java.io.File
import java.security.MessageDigest
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * This function is used to check if an application is rooted.
 * @return Boolean true if is root false if not.
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
 * @return Boolean true if is emulated false if not.
 */
fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic", true) || Build.FINGERPRINT.startsWith(
        "unknown",
        true
    ) || Build.MODEL.contains(
        "google_sdk", true
    ) || Build.MODEL.contains("Emulator", true) || Build.MODEL.contains(
        "Android SDK built for x86",
        true
    ) || Build.MANUFACTURER.contains(
        "Genymotion", true
    ) || Build.BRAND.startsWith("generic", true) && Build.DEVICE.startsWith(
        "generic",
        true
    ) || "google_sdk" == Build.PRODUCT || (Build.HARDWARE == "goldfish" && Build.BOOTLOADER == "unknown") || (Build.BOOTLOADER == "unknown" && Build.BRAND.startsWith(
        "generic", true
    )) || (Build.DEVICE.startsWith("generic", true) && Build.PRODUCT == "sdk"))
}

/**
 * This function is used to check if an application is in debugging
 * @param playStoreAppId the expected installer or source package.
 * @param context of the application for the packageManager.
 * @return Boolean true if app is downloaded from Store false if not.
 */
fun isDownloadedFromStore(playStoreAppId: String, context: Context): Boolean {
    val installer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName
    } else {
        context.packageManager.getInstallerPackageName(context.packageName)
    }
    return installer != null && installer.startsWith(playStoreAppId)
}

/**
 * This function is used to check whether an application is being handled at runtime
 * @return Boolean true if is the app is being handled at runtime false if not.
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
 * @param regex regular expression to compare
 * @param text data to validate
 * @return Boolean true if is the data is a valid expression false if not.
 */
fun isValidRegex(regex: String, text: String): Boolean {
    return text.isNotEmpty() && Pattern.compile(regex).matcher(text).matches()
}

/**
 * This function is used to prevent screenshots and screen recording while the application is in the
 * foreground.
 * @param activity to prevent screenshots
 */
fun preventScreenshots(activity: Activity) {
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

/**
 * This function is used to store data securely using Secure Shared Preferences.
 * @param context
 * @param key under which the value will be stored in the SharedPreferences.
 * @param value the value that will be stored in the SharedPreferences under the specified key.
 * @param fileName the name of the preferences file where the data will be stored.
 * */
fun storeSecureSharedPreferences(context: Context, key: String, value: String, fileName: String) {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        fileName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

/**
 * This function is used to detect if the origin of the package is potencially dangerous compared
 * to a list of dangerous origins.
 * @param context of the application for the packageManager.
 * @param additionalPotentiallyDangerousApps list of dangerous origin.
 * @return Boolean true if is the origin is dangerous false if not.
 * */
fun detectPotentiallyDangerousOrigin(context:Context, additionalPotentiallyDangerousApps: List<String> = emptyList()): Boolean {
    val packages = SecurityConstants.knownDangerousAppsPackages.toMutableList()
    packages.addAll(additionalPotentiallyDangerousApps)
    return isAnyPackageFromListInstalled(context,packages)
}

/**
 * This function is used to encrypt or decrypt data using a secret key and a transformation method.
 * @param data to encrypt/decrypt
 * @param transformation  specifies the cryptographic transformation to be used .
 * @param mode for the cipher, default value is Cipher.ENCRYPT_MODE. Other possible value is Cipher.DECRYPT_MODE
 * @return ByteArray of the encrypted/decrypted data
 */
fun cryptData(
    data: ByteArray,
    transformation: String,
    mode: Int = Cipher.ENCRYPT_MODE
): ByteArray {
    val cipher = Cipher.getInstance(transformation)
    cipher.init(mode, generateSecretKey())
    return cipher.doFinal(data)
}

/**
 * Generates an SHA-256 hash for a byte array.
 * @return ByteArray of the generated hash.
 */
fun generateHash(data: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(data)
}

/**
 * This function is used to generate a secret key, which can be used for data encryption and decryption.
 *  @return SecretKey the generated key that can be used for encryption and decryption operations.
 */
private fun generateSecretKey(): SecretKey {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(256)
    return keyGenerator.generateKey()
}

/**
 * This function is used to check from a list of packages if it is installed in the device.
 * @param context of the application for the packageManager.
 * @param packages list of packages.
 * @return Boolean true if it is installed false if not.
 */
private fun isAnyPackageFromListInstalled(context: Context, packages: List<String>): Boolean {
    var result = false

    val packageManager: PackageManager = context.packageManager

    for (packageName in packages) {
        try {
            packageManager.getPackageInfo(packageName, 0)
            Log.e(
                "isAnyPackageInstalled",
                "$packageName ROOT management app detected!"
            )
            result = true
        } catch (exception: PackageManager.NameNotFoundException) {
            // Exception thrown, package is not installed into the system
        }
    }
    return result
}