package com.rocket.android.core.data.map.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.rocket.android.core.data.map.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.map.extensions.bitmapDescriptorFromVectorHMS
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.huawei.hms.maps.model.MarkerOptions as MarkerOptionsHMS
import com.rocket.android.core.data.gmsmap.model.MarkerOptions as MarkerOptionsGMS

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
                    gmsMarkerOptions = MarkerOptionsGMS(context = context)
                }
            }
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS -> {

                gmsMarkerOptions = MarkerOptionsGMS(context = context)
            }
            else -> {
                gmsMarkerOptions = MarkerOptionsGMS(context = context)
            }
        }
    }

    fun setPosition(position: LatLng) {
        gmsMarkerOptions?.setPosition(position.toLatLngGMS())
        hmsMarkerOptions?.position(position.hmsLatLng)
    }

    fun setIcon(@DrawableRes icon: Int, @StyleRes theme: Int = -1) {
        gmsMarkerOptions?.setIcon(
            icon = icon, theme = theme
        )
        hmsMarkerOptions?.icon(
            bitmapDescriptorFromVectorHMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        gmsMarkerOptions?.setIcon(bitmapDescriptor = bitmapDescriptor.gmsBitmapDescriptor!!)
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

    fun setTitle(title: String) {
        gmsMarkerOptions?.setTitle(title = title)
        hmsMarkerOptions?.title(title)
    }

    fun setSnippet(snippet: String) {
        gmsMarkerOptions?.setSnippet(snippet = snippet)
        hmsMarkerOptions?.snippet(snippet)
    }

    fun setDraggable(draggable: Boolean) {
        gmsMarkerOptions?.setDraggable(draggable = draggable)
        hmsMarkerOptions?.draggable(draggable)
    }

    fun setVisible(visible: Boolean) {
        gmsMarkerOptions?.setVisible(visible = visible)
        hmsMarkerOptions?.visible(visible)
    }

    private fun MarkerOptions.toMarkerOptionsGMS(context: Context): MarkerOptionsGMS = with(this) {
        MarkerOptionsGMS(context)
    }

    fun List<MarkerOptions>.toMarkerOptionsGMSList(context: Context): List<MarkerOptionsGMS> =
        with(this) {
            this.map { it.toMarkerOptionsGMS(context = context) }
        }
}
