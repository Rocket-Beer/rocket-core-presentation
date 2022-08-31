package com.rocket.android.core.data.gmsmap.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromUrl
import com.rocket.android.core.data.gmsmap.extensions.bitmapDescriptorFromVectorGMS
import com.google.android.gms.maps.model.MarkerOptions as MarkerOptionsGMS

class MarkerOptions(
    private val context: Context
) {
    var id: String = ""

    var gmsMarkerOptions: MarkerOptionsGMS? = null
        private set

    init {
        gmsMarkerOptions = MarkerOptionsGMS()
    }

    fun setPosition(position: LatLng) {
        gmsMarkerOptions?.position(position.gmsLatLng)
    }

    fun setIcon(@DrawableRes icon: Int, @StyleRes theme: Int = -1) {
        gmsMarkerOptions?.icon(
            bitmapDescriptorFromVectorGMS(
                context = context,
                vectorResId = icon,
                themeResId = theme
            )
        )
    }

    fun setIcon(bitmapDescriptor: BitmapDescriptor) {
        gmsMarkerOptions?.icon(bitmapDescriptor.gmsBitmapDescriptor)
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
        gmsMarkerOptions?.title(title)
    }

    fun setSnippet(snippet: String) {
        gmsMarkerOptions?.snippet(snippet)
    }

    fun setDraggable(draggable: Boolean) {
        gmsMarkerOptions?.draggable(draggable)
    }

    fun setVisible(visible: Boolean) {
        gmsMarkerOptions?.visible(visible)
    }
}
