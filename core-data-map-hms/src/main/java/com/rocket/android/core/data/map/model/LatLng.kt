package com.rocket.android.core.data.map.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.huawei.hms.maps.model.LatLng as LatLngHMS

/**
 * Class which represents a pair of latitude and longitude coordinates
 *
 * Contains an instance of [LatLngHMS]
 *
 * @property hmsLatLng instance of [LatLngHMS] built from [latitude] and [longitude]
 */
@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double,
) : Parcelable {

    val hmsLatLng: LatLngHMS by lazy {
        LatLngHMS(latitude, longitude)
    }
}

/**
 * Creates a [LatLng] from a [LatLngHMS]
 * @return the [LatLng] created
 */
fun LatLngHMS.toLatLng() = LatLng(latitude = this.latitude, longitude = this.longitude)
