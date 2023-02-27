package com.rocket.android.core.data.gmsmap.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromVectorGMS
import com.rocket.android.core.data.gmsmap.extensions.toLatLng
import com.google.android.gms.maps.model.Marker as MarkerGMS

/**
 * Class which represents a marker on a map
 *
 * Contains an instance of [MarkerGMS] as well as methods to manage it
 *
 * @property gmsMarker instance of [MarkerGMS]
 */
class Marker {
    var gmsMarker: MarkerGMS? = null

    /**
     * Gets the tag of [gmsMarker]
     * @return tag of [gmsMarker] or null if it does not exist
     */
    fun getTag(): Any? {
        return try {
            gmsMarker?.tag
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Sets the tag for [gmsMarker]
     * @param tag the tag for the marker
     */
    fun setTag(tag: String) {
        gmsMarker?.tag = tag
    }

    /**
     * Gets the visibility of [gmsMarker]
     * @return visibility of the marker
     */
    fun isVisible(): Boolean? {
        return try {
            gmsMarker?.isVisible
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Sets the visibility of [gmsMarker]
     * @param visible visibility of the marker
     */
    fun setVisible(visible: Boolean) {
        gmsMarker?.isVisible = visible
    }

    /**
     * Gets the position of [gmsMarker]
     * @return a [LatLng] object holding the marker's current position
     */
    fun getPosition(): LatLng? {
        return try {
            gmsMarker?.position?.toLatLng()
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Sets the icon for [gmsMarker] by calling [bitmapDescriptorFromVectorGMS] with the parameters provided
     * @param context *context* parameter of [bitmapDescriptorFromVectorGMS]
     * @param icon *vectorResId* parameter of [bitmapDescriptorFromVectorGMS]
     * @param theme *themeResId* parameter of [bitmapDescriptorFromVectorGMS]
     */
    fun setIcon(context: Context, @DrawableRes icon: Int, @StyleRes theme: Int = -1) {
        gmsMarker?.setIcon(
            bitmapDescriptorFromVectorGMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    /**
     * Sets the icon for [gmsMarker] from a [BitmapDescriptor]
     * @param bitmapDescriptor [BitmapDescriptor] used for the icon
     */
    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        gmsMarker?.setIcon(bitmapDescriptor.gmsBitmapDescriptor)
    }

    /**
     * Sets the icon for [gmsMarker] by calling [bitmapDescriptorFromUrl] with the parameters provided
     * @param context *context* parameter of [bitmapDescriptorFromUrl]
     * @param url *iconUrl* parameter of [bitmapDescriptorFromUrl]
     * @param width *width* parameter of [bitmapDescriptorFromUrl]
     * @param height *height* parameter of [bitmapDescriptorFromUrl]
     */
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
     * Removes [gmsMarker] from the map
     */
    fun remove() {
        gmsMarker?.remove()
    }

    /**
     * Gets the title of [gmsMarker]
     * @return title of [gmsMarker] or null if it does not exist
     */
    fun getTitle(): String? {
        return try {
            gmsMarker?.title
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Gets the snippet of [gmsMarker]
     * @return the snippet of [gmsMarker] or null if it does not exist
     */
    fun getSnippet(): String? {
        return gmsMarker?.snippet
    }
}

/**
 * Creates a [Marker] from this marker
 * @return a [Marker] object with this marker as its [Marker.gmsMarker]
 */
fun MarkerGMS.toMarker() = Marker().also { it.gmsMarker = this }
