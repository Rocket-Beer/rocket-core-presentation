package com.rocket.android.core.data.gmsmap.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromVectorGMS
import com.rocket.android.core.data.gmsmap.extensions.toLatLng
import com.google.android.gms.maps.model.Marker as MarkerGMS

class Marker {
    var gmsMarker: MarkerGMS? = null

    fun getTag(): Any? {
        return try {
            gmsMarker?.tag
        } catch (_: Throwable) {
            null
        }
    }

    fun setTag(tag: String) {
        gmsMarker?.tag = tag
    }

    fun isVisible(): Boolean? {
        return try {
            gmsMarker?.isVisible
        } catch (_: Throwable) {
            null
        }
    }

    fun setVisible(visible: Boolean) {
        gmsMarker?.isVisible = visible
    }

    fun getPosition(): LatLng? {
        return try {
            gmsMarker?.position?.toLatLng()
        } catch (_: Throwable) {
            null
        }
    }

    fun setIcon(context: Context, @DrawableRes icon: Int, @StyleRes theme: Int = -1) {
        gmsMarker?.setIcon(
            bitmapDescriptorFromVectorGMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        gmsMarker?.setIcon(bitmapDescriptor.gmsBitmapDescriptor)
    }

    fun setIcon(context: Context, url: String, width: Int = -1, height: Int = -1) {
        bitmapDescriptorFromUrl(
            context = context,
            iconUrl = url,
            width = width,
            height = height
        )?.let {
            setIcon(bitmapDescriptor = it)
        }
    }

    fun remove() {
        gmsMarker?.remove()
    }

    fun getTitle(): String? {
        return try {
            gmsMarker?.title
        } catch (_: Throwable) {
            null
        }
    }

    fun getSnippet(): String? {
        return gmsMarker?.snippet
    }
}

fun MarkerGMS.toMarker() = Marker().also { it.gmsMarker = this }
