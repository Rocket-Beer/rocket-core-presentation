package com.rocket.android.core.data.gmsmap.model

data class CustomCircleOptions(
    val center: LatLng,
    val radius: Double,
    val strokeColor: Int,
    val fillColor: Int,
    val strokeWidth: Float,
    val zIndex: Float,
    val visible: Boolean = true
)
