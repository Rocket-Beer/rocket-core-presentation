package com.rocket.android.core.data.gmsmap.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.android.gms.maps.model.LatLng as LatLngGMS

/**
 * Class which represents a pair of latitude and longitude coordinates
 *
 * Contains an instance of [LatLngGMS]
 *
 * @param latitude for latitude position
 * @param longitude or longitude position
 * @property gmsLatLng instance of [LatLngGMS] built from [latitude] and [longitude]
 */
@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    val gmsLatLng: LatLngGMS by lazy {
        LatLngGMS(latitude, longitude)
    }
}
