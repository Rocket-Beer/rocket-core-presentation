package com.rocket.android.core.data.hmsmap.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.hmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.hmsmap.extensions.bitmapDescriptorFromVectorHMS
import com.rocket.android.core.data.hmsmap.extensions.isHmsCoreVersionAvailable
import com.huawei.hms.maps.model.MarkerOptions as MarkerOptionsHMS

/**
 * Class which holds some options applied to a marker on a map, such as its position or icon
 *
 * Contains an instance of [MarkerOptionsHMS] as well as methods to manage it
 *
 * @property hmsMarkerOptions instance of [MarkerOptionsHMS], which defines options for a marker
 */
class MarkerOptions(
    private val context: Context,
) {
    var id: String = ""
    var hmsMarkerOptions: MarkerOptionsHMS? = null
        private set

    init {
        if (isHmsCoreVersionAvailable(context = context)) {
            hmsMarkerOptions = MarkerOptionsHMS()
        }
    }

    /**
     * Sets the location for [hmsMarkerOptions]
     * @param position [LatLng] object with the position for [hmsMarkerOptions]
     */
    fun setPosition(position: LatLng) {
        hmsMarkerOptions?.position(position.hmsLatLng)
    }

    /**
     * Sets the icon for [hmsMarkerOptions]
     * @param icon id of the VectorDrawableCompat
     * @param theme id of the Theme to be applied to the VectorDrawableCompat. If it is -1 or a parsing error is found,
     * the returned drawable will be styled for the context theme
     */
    fun setIcon(@DrawableRes icon: Int, @StyleRes theme: Int = -1) {
        hmsMarkerOptions?.icon(
            bitmapDescriptorFromVectorHMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    /**
     * Sets the icon for [hmsMarkerOptions]
     * @param bitmapDescriptor [BitmapDescriptor] to be used for setting the icon
     */
    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        hmsMarkerOptions?.icon(bitmapDescriptor.hmsBitmapDescriptor)
    }

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
     * Sets the title for [hmsMarkerOptions]
     * @param title title to be used
     */
    fun setTitle(title: String) {
        hmsMarkerOptions?.title(title)
    }

    /**
     * Sets the snippet for [hmsMarkerOptions]
     * @param snippet snippet to be used
     */
    fun setSnippet(snippet: String) {
        hmsMarkerOptions?.snippet(snippet)
    }

    /**
     * Sets the draggability for [hmsMarkerOptions]
     * @param draggable indicates whether [hmsMarkerOptions] is draggable
     */
    fun setDraggable(draggable: Boolean) {
        hmsMarkerOptions?.draggable(draggable)
    }

    /**
     * Sets the visibility for [hmsMarkerOptions]
     * @param visible indicates whether [hmsMarkerOptions] is visible
     */
    fun setVisible(visible: Boolean) {
        hmsMarkerOptions?.visible(visible)
    }
}
