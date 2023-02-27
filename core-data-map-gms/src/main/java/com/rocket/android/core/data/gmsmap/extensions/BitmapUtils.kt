package com.rocket.android.core.data.gmsmap.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.rocket.android.core.data.gmsmap.model.BitmapDescriptor
import com.rocket.android.core.data.gmsmap.model.toBitmapDescription
import java.net.URL
import com.google.android.gms.maps.model.BitmapDescriptor as BitmapDescriptorGMS
import com.google.android.gms.maps.model.BitmapDescriptorFactory as BitmapDescriptorFactoryGMS

/**
 * Creates a [BitmapDescriptorGMS] from a [VectorDrawableCompat]
 * @param context [Context] to access drawable and style resources
 * @param vectorResId id of the [VectorDrawableCompat]
 * @param themeResId id of the [Theme] to be applied to the [VectorDrawableCompat]. If it is -1 or a parsing
 * error is found, the returned drawable will be styled for the [context] theme
 * @return the [BitmapDescriptorGMS] created
 */
fun bitmapDescriptorFromVectorGMS(
    context: Context,
    @DrawableRes vectorResId: Int,
    @StyleRes themeResId: Int
): BitmapDescriptorGMS? {
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
            BitmapDescriptorFactoryGMS.fromBitmap(bitmap)
        } else {
            ContextCompat.getDrawable(context, vectorResId)?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                val bitmap =
                    Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
                draw(Canvas(bitmap))
                BitmapDescriptorFactoryGMS.fromBitmap(bitmap)
            }
        }
    } else {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactoryGMS.fromBitmap(bitmap)
        }
    }
}

/**
 * Creates a [BitmapDescriptor] from a [BitmapDescriptorGMS] built with [bitmapDescriptorFromVectorGMS]
 * @param context *context* param in [bitmapDescriptorFromVectorGMS]
 * @param vectorResId *vectorResId* param in [bitmapDescriptorFromVectorGMS]
 * @param themeResId *themeResId* param in [bitmapDescriptorFromVectorGMS]
 * @return the [BitmapDescriptor] created
 */
fun bitmapDescriptorFromVector(
    context: Context,
    @DrawableRes vectorResId: Int,
    @StyleRes themeResId: Int
): BitmapDescriptor? {
    return bitmapDescriptorFromVectorGMS(
        context = context,
        vectorResId = vectorResId,
        themeResId = themeResId
    )?.toBitmapDescription()
}


/**
 * Creates a [BitmapDescriptor] from a given url which holds a [Bitmap]. The bitmap will be created
 * from the Google Maps Services library
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

        BitmapDescriptorFactoryGMS.fromBitmap(bitmap).toBitmapDescription()
    } catch (_: Throwable) {
        null
    }
