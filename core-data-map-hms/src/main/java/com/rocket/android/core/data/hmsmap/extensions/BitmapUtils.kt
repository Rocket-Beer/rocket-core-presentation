package com.rocket.android.core.data.hmsmap.extensions

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources.Theme
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.rocket.android.core.data.hmsmap.model.BitmapDescriptor
import com.rocket.android.core.data.hmsmap.model.toBitmapDescription
import java.net.URL
import com.huawei.hms.maps.model.BitmapDescriptor as BitmapDescriptorHMS
import com.huawei.hms.maps.model.BitmapDescriptorFactory as BitmapDescriptorFactoryHMS


/**
 * Creates a [BitmapDescriptorHMS] from a [VectorDrawableCompat]
 * @param context [Context] to access drawable and style resources
 * @param vectorResId id of the [VectorDrawableCompat]
 * @param themeResId id of the [Theme] to be applied to the [VectorDrawableCompat]. If it is -1 or a parsing
 * error is found, the returned drawable will be styled for the [context] theme
 * @return the [BitmapDescriptorHMS] created
 */
fun bitmapDescriptorFromVectorHMS(
    context: Context,
    @DrawableRes vectorResId: Int,
    @StyleRes themeResId: Int
): BitmapDescriptorHMS? {
    if (themeResId != -1) {
        val wrapper = ContextThemeWrapper(context, themeResId)
        val vectorDrawable =
            VectorDrawableCompat.create(context.resources, vectorResId, wrapper.theme)

        return if (vectorDrawable != null) {
            vectorDrawable.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            BitmapDescriptorFactoryHMS.fromBitmap(bitmap)
        } else {
            ContextCompat.getDrawable(context, vectorResId)?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                val bitmap =
                    Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
                draw(Canvas(bitmap))
                BitmapDescriptorFactoryHMS.fromBitmap(bitmap)
            }
        }
    } else {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactoryHMS.fromBitmap(bitmap)
        }
    }
}

/**
 * Creates a [BitmapDescriptor] from a [BitmapDescriptorHMS] built with [bitmapDescriptorFromVectorHMS]
 * @param context *context* param in [bitmapDescriptorFromVectorHMS]
 * @param vectorResId *vectorResId* param in [bitmapDescriptorFromVectorHMS]
 * @param themeResId *themeResId* param in [bitmapDescriptorFromVectorHMS]
 * @return the [BitmapDescriptor] created
 */
fun bitmapDescriptorFromVector(
    context: Context,
    @DrawableRes vectorResId: Int,
    @StyleRes themeResId: Int
): BitmapDescriptor? {
    return if (isHmsCoreVersionAvailable(context = context)) {
        bitmapDescriptorFromVectorHMS(
            context = context,
            vectorResId = vectorResId,
            themeResId = themeResId
        )?.toBitmapDescription()
    } else null
}

/**
 * Creates a [BitmapDescriptor] from a given url which holds a [Bitmap]. If version number of the package
 * *com.huawei.hwid* is lower than 5.0.0.003-01, the bitmap will be created from the Google Maps Services library
 * @param context current [Context]
 * @param iconUrl url containing a [Bitmap]
 * @param width if this value and [height] are different from -1, the [Bitmap] will be scaled
 * @param height if this value and [width] are different from -1, the [Bitmap] will be scaled
 * @return the [BitmapDescriptor] created
 */
fun bitmapDescriptorFromUrl(
    context: Context,
    iconUrl: String,
    width: Int = -1,
    height: Int = -1
): BitmapDescriptor? =
    try {
        val url = URL(iconUrl)
        var bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

        if (width != -1 && height != -1) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        }

        if (isHmsCoreVersionAvailable(context = context)) {
            BitmapDescriptorFactoryHMS.fromBitmap(bitmap).toBitmapDescription()
        } else null
    } catch (_: Throwable) {
        null
    }

/**
 * Checks version number of the package *com.huawei.hwid*
 * @param context [Context] to obtain access to the [PackageManager]
 * @return true if package version is equal or greater than 5.0.0.003-01, if not false
 */
fun isHmsCoreVersionAvailable(context: Context): Boolean {
    return try {
        val pm: PackageManager = context.packageManager
        val packageInfo: PackageInfo = pm.getPackageInfo("com.huawei.hwid", 0)
        val version: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
        version >= 50000301L
    } catch (e: Exception) {
        false
    }
}
