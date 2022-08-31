package com.rocket.android.core.data.gmsmap.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.android.gms.maps.model.LatLng as LatLngGMS

@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    val gmsLatLng: LatLngGMS by lazy {
        LatLngGMS(latitude, longitude)
    }
}
