package com.rocket.android.core.data.gmsmap.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromVectorGMS
import com.google.android.gms.maps.model.MarkerOptions as MarkerOptionsGMS

/**
 * Class which holds some options applied to a marker on a map, such as its position or icon
 *
 * Contains an instance of [MarkerOptionsGMS] as well as methods to manage it
 *
 * @property gmsMarkerOptions instance of [MarkerOptionsGMS], which defines options for a marker
 */
class MarkerOptions(
    private val context: Context
) {
    var id: String = ""

    var gmsMarkerOptions: MarkerOptionsGMS? = null
        private set

    init {
        gmsMarkerOptions = MarkerOptionsGMS()
    }

    /**
     * Sets the location for [gmsMarkerOptions]
     * @param position [LatLng] object with the position for [gmsMarkerOptions]
     */
    fun setPosition(position: LatLng) {
        gmsMarkerOptions?.position(position.gmsLatLng)
    }

    /**
     * Sets the icon for [gmsMarkerOptions]
     * @param icon id of the VectorDrawableCompat
     * @param theme id of the Theme to be applied to the VectorDrawableCompat. If it is -1 or a parsing error is found,
     * the returned drawable will be styled for the context theme
     */
    fun setIcon(@DrawableRes icon: Int, @StyleRes theme: Int = -1) {
        gmsMarkerOptions?.icon(
            bitmapDescriptorFromVectorGMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    /**
     * Sets the icon for [gmsMarkerOptions]
     * @param bitmapDescriptor [BitmapDescriptor] to be used for setting the icon
     */
    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        gmsMarkerOptions?.icon(bitmapDescriptor.gmsBitmapDescriptor)
    }

    /**
     * Sets the icon for [gmsMarkerOptions]
     * @param url url to be used for setting the icon
     */
    fun setIcon(url: String, width: Int = -1, height: Int = -1) {
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
     * Sets the title for [gmsMarkerOptions]
     * @param title title to be used
     */
    fun setTitle(title: String) {
        gmsMarkerOptions?.title(title)
    }

    /**
     * Sets the snippet for [gmsMarkerOptions]
     * @param snippet snippet to be used
     */
    fun setSnippet(snippet: String) {
        gmsMarkerOptions?.snippet(snippet)
    }

    /**
     * Sets the draggability for [gmsMarkerOptions]
     * @param draggable indicates whether [gmsMarkerOptions] is draggable
     */
    fun setDraggable(draggable: Boolean) {
        gmsMarkerOptions?.draggable(draggable)
    }

    /**
     * Sets the visibility for [gmsMarkerOptions]
     * @param visible indicates whether [gmsMarkerOptions] is visible
     */
    fun setVisible(visible: Boolean) {
        gmsMarkerOptions?.visible(visible)
    }
}
