import android.content.Context
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dalvik.system.DexClassLoader
import java.io.File
import java.security.MessageDigest
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

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
    /*TODO Review format code*/ return (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") || "google_sdk" == Build.PRODUCT || (Build.HARDWARE == "goldfish" && Build.BOOTLOADER == "unknown") || (Build.BOOTLOADER == "unknown" && Build.BRAND.startsWith("generic")) || (Build.DEVICE.startsWith("generic") && Build.PRODUCT == "sdk"))
}

/**
 * This function is used to check if an application is in debugging
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
fun isValidRegex(regex: String, text: String): Boolean {
    return text.isNotEmpty() && Pattern.compile(regex).matcher(text).matches()
}

/**
 * This function is used to store data securely using Secure Shared Preferences.
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
 * This function is used to generate a secret key, which can be used for data encryption and decryption.
 */
fun generateSecretKey(): SecretKey {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(256)
    return keyGenerator.generateKey()
}

/**
 * This function is used to encrypt data using a secret key and a transformation method.
 */
/*TODO Revisar funciones encrypt y decrypt*/
fun encryptData(data: ByteArray, transformation: String): ByteArray {
    val cipher = Cipher.getInstance(transformation)
    cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey())
    return cipher.doFinal(data)
}

/**
 * This function is used to decrypt data using a secret key and a transformation method.
 */
fun decryptData(encryptedData: ByteArray, transformation: String): ByteArray {
    val cipher = Cipher.getInstance(transformation)
    cipher.init(Cipher.DECRYPT_MODE, generateSecretKey())
    return cipher.doFinal(encryptedData)
}

/**
 * Generates an SHA-256 hash for a byte array.
 */
fun generateHash(data: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(data)
}