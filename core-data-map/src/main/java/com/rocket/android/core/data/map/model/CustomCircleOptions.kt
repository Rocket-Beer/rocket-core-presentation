package com.rocket.android.core.data.map.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.huawei.hms.maps.model.CircleOptions as CircleOptionsHMS
import com.rocket.android.core.data.gmsmap.model.CustomCircleOptions as CircleOptionsGMS
import com.rocket.android.core.data.gmsmap.model.LatLng as LatLngGMS

@Parcelize
data class CustomCircleOptions(
    val center: LatLng,
    val radius: Double,
    val strokeColor: Int,
    val fillColor: Int,
    val strokeWidth: Float,
    val zIndex: Float,
    val visible: Boolean = true
) : Parcelable {
    val circleOptionsHMS: CircleOptionsHMS by lazy {
        CircleOptionsHMS()
            .center(center.hmsLatLng)
            .radius(radius)
            .strokeColor(strokeColor)
            .strokeWidth(strokeWidth)
            .fillColor(fillColor)
            .zIndex(zIndex)
            .visible(visible)
    }
}

fun CustomCircleOptions.toCustomCircleOptionsGMS() = CircleOptionsGMS(
    center = this.center.toLatLngGMS(),
    radius = this.radius,
    strokeColor = this.strokeColor,
    fillColor = this.fillColor,
    strokeWidth = this.strokeWidth,
    zIndex = this.zIndex,
    visible = this.visible
)

fun LatLng.toLatLngGMS() = LatLngGMS(latitude = this.latitude, longitude = this.longitude)
