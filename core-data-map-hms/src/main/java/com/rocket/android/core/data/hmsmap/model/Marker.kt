package com.rocket.android.core.data.hmsmap.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.hmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.hmsmap.extensions.bitmapDescriptorFromVectorHMS
import com.huawei.hms.maps.model.Marker as MarkerHMS

/**
 * Class which represents a marker on a map
 *
 * Contains an instance of [MarkerHMS] as well as methods to manage it
 *
 * @property hmsMarker instance of [MarkerHMS]
 */
class Marker {
    var hmsMarker: MarkerHMS? = null

    /**
     * Gets the tag of [hmsMarker]
     * @return tag of [hmsMarker] or null if it does not exist
     */
    fun getTag(): Any? {
        return try {
            hmsMarker?.tag
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Sets the tag for [hmsMarker]
     * @param tag the tag for the marker
     */
    fun setTag(tag: String) {
        hmsMarker?.tag = tag
    }

    /**
     * Gets the visibility of [hmsMarker]
     * @return visibility of the marker
     */
    fun isVisible(): Boolean? {
        return try {
            hmsMarker?.isVisible
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Sets the visibility of [hmsMarker]
     * @param visible visibility of the marker
     */
    fun setVisible(visible: Boolean) {
        hmsMarker?.isVisible = visible
    }

    /**
     * Gets the position of [hmsMarker]
     * @return a [LatLng] object holding the marker's current position
     */
    fun getPosition(): LatLng? {
        return try {
            hmsMarker?.position?.toLatLng()
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Sets the icon for [hmsMarker] by calling [bitmapDescriptorFromVectorHMS] with the parameters provided
     * @param context *context* parameter of [bitmapDescriptorFromVectorHMS]
     * @param icon *vectorResId* parameter of [bitmapDescriptorFromVectorHMS]
     * @param theme *themeResId* parameter of [bitmapDescriptorFromVectorHMS]
     */
    fun setIcon(context: Context, @DrawableRes icon: Int, @StyleRes theme: Int = -1) {

        hmsMarker?.setIcon(
            bitmapDescriptorFromVectorHMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    /**
     * Sets the icon for [hmsMarker] from a [BitmapDescriptor]
     * @param bitmapDescriptor [BitmapDescriptor] used for the icon
     */
    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        hmsMarker?.setIcon(bitmapDescriptor.hmsBitmapDescriptor)
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

    /**
     * Removes [hmsMarker] from the map
     */
    fun remove() {
        hmsMarker?.remove()
    }

    /**
     * Gets the title of [hmsMarker]
     * @return title of [hmsMarker] or null if it does not exist
     */
    fun getTitle(): String? {
        return try {
            hmsMarker?.title
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Gets the snippet of [hmsMarker]
     * @return the snippet of [hmsMarker] or null if it does not exist
     */
    fun getSnippet(): String? {
        return hmsMarker?.snippet
    }
}

/**
 * Creates a [Marker] from this marker
 * @return a [Marker] object with this marker as its [Marker.hmsMarker]
 */
fun MarkerHMS.toMarker() = Marker().also { it.hmsMarker = this }
