package com.rocket.android.core.data.gmsmap.model

import com.google.android.gms.maps.model.LatLng

data class CustomCircleOptions(
    val center: LatLng,
    val radius: Double,
    val strokeColor: Int,
    val fillColor: Int,
    val strokeWidth: Float
)
