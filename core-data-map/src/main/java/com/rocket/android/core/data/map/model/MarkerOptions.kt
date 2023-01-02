package com.rocket.android.core.data.map.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.rocket.android.core.data.map.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.map.extensions.bitmapDescriptorFromVectorGMS
import com.rocket.android.core.data.map.extensions.bitmapDescriptorFromVectorHMS
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.google.android.gms.maps.model.MarkerOptions as MarkerOptionsGMS
import com.huawei.hms.maps.model.MarkerOptions as MarkerOptionsHMS

/**
 * TODO
 *
 * TODO
 *
 * @property hmsMarkerOptions Instance of [MarkerOptionsHMS], which defines options for a marker
 */
class MarkerOptions(
    private val context: Context
) {
    var id: String = ""

    var gmsMarkerOptions: MarkerOptionsGMS? = null
        private set

    var hmsMarkerOptions: MarkerOptionsHMS? = null
        private set

    init {
        when {
            HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(context) == com.huawei.hms.api.ConnectionResult.SUCCESS -> {
                if (isHmsCoreVersionAvailable(context = context)) {
                    hmsMarkerOptions = MarkerOptionsHMS()
                } else {
                    gmsMarkerOptions = MarkerOptionsGMS()
                }
            }
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS -> {

                gmsMarkerOptions = MarkerOptionsGMS()
            }
            else -> {
                gmsMarkerOptions = MarkerOptionsGMS()
            }
        }
    }

    /**
     * Sets the location for [hmsMarkerOptions]
     * @param position [LatLng] object with the position for [hmsMarkerOptions]
     */
    fun setPosition(position: LatLng) {
        gmsMarkerOptions?.position(position.gmsLatLng)
        hmsMarkerOptions?.position(position.hmsLatLng)
    }

    /**
     * Sets the icon for [hmsMarkerOptions]
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
        gmsMarkerOptions?.icon(bitmapDescriptor.gmsBitmapDescriptor)
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
        gmsMarkerOptions?.title(title)
        hmsMarkerOptions?.title(title)
    }

    /**
     * Sets the snippet for [hmsMarkerOptions]
     * @param snippet snippet to be used
     */
    fun setSnippet(snippet: String) {
        gmsMarkerOptions?.snippet(snippet)
        hmsMarkerOptions?.snippet(snippet)
    }

    /**
     * Sets the draggability for [hmsMarkerOptions]
     * @param draggable indicates whether [hmsMarkerOptions] is draggable
     */
    fun setDraggable(draggable: Boolean) {
        gmsMarkerOptions?.draggable(draggable)
        hmsMarkerOptions?.draggable(draggable)
    }

    /**
     * Sets the visibility for [hmsMarkerOptions]
     * @param visible indicates whether [hmsMarkerOptions] is visible
     */
    fun setVisible(visible: Boolean) {
        gmsMarkerOptions?.visible(visible)
        hmsMarkerOptions?.visible(visible)
    }
}
