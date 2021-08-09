package com.rocket.android.core.data.map.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.android.gms.maps.model.LatLng as LatLngGMS
import com.huawei.hms.maps.model.LatLng as LatLngHMS

@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    val gmsLatLng: LatLngGMS by lazy {
        LatLngGMS(latitude, longitude)
    }

    val hmsLatLng: LatLngHMS by lazy {
        LatLngHMS(latitude, longitude)
    }
}

fun LatLngGMS.toLatLng() = LatLng(latitude = this.latitude, longitude = this.longitude)
fun LatLngHMS.toLatLng() = LatLng(latitude = this.latitude, longitude = this.longitude)
