package com.rocket.android.core.data.map.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromVectorGMS
import com.rocket.android.core.data.gmsmap.model.toBitmapDescription
import com.rocket.android.core.data.map.extensions.bitmapDescriptorFromVectorHMS
import com.huawei.hms.maps.model.Marker as MarkerHMS
import com.rocket.android.core.data.gmsmap.model.Marker as MarkerGMS

class Marker {
    var gmsMarker: MarkerGMS? = null
    var hmsMarker: MarkerHMS? = null

    fun getTag(): Any? {
        return try {
            gmsMarker?.getTag() ?: hmsMarker?.tag
        } catch (_: Throwable) {
            null
        }
    }

    fun setTag(tag: String) {
        gmsMarker?.setTag(tag = tag)
        hmsMarker?.tag = tag
    }

    fun isVisible(): Boolean? {
        return try {
            gmsMarker?.isVisible() ?: hmsMarker?.isVisible
        } catch (_: Throwable) {
            null
        }
    }

    fun setVisible(visible: Boolean) {
        gmsMarker?.setVisible(visible = visible)
        hmsMarker?.isVisible = visible
    }

    fun getPosition(): LatLng? {
        return try {
            gmsMarker?.getPosition()?.toLatLng() ?: hmsMarker?.position?.toLatLng()
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
            )!!.toBitmapDescription()
        )

        hmsMarker?.setIcon(
            bitmapDescriptorFromVectorHMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        gmsMarker?.setIcon(bitmapDescriptor.gmsBitmapDescriptor!!)
        hmsMarker?.setIcon(bitmapDescriptor.hmsBitmapDescriptor)
    }

    fun setIcon(context: Context, url: String, width: Int = -1, height: Int = -1) {
        bitmapDescriptorFromUrl(
            context = context,
            iconUrl = url,
            width = width,
            height = height
        )?.let {
            setIcon(bitmapDescriptor = BitmapDescriptor(it))
        }
    }

    fun remove() {
        gmsMarker?.remove()
        hmsMarker?.remove()
    }

    fun getTitle(): String? {
        return try {
            gmsMarker?.getTitle() ?: hmsMarker?.title
        } catch (_: Throwable) {
            null
        }
    }

    fun getSnippet(): String? {
        return gmsMarker?.getSnippet() ?: hmsMarker?.snippet
    }
}

fun MarkerGMS.toMarker() = Marker().also { it.gmsMarker = this }

fun MarkerHMS.toMarker() = Marker().also { it.hmsMarker = this }
