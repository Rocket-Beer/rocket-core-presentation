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
