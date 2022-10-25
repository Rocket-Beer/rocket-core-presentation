package com.rocket.android.core.data.gmsmap.model

import com.google.android.gms.maps.model.BitmapDescriptor as GmsBitmapDescriptor

class BitmapDescriptor(
    val gmsBitmapDescriptor: GmsBitmapDescriptor? = null,
)

fun GmsBitmapDescriptor.toBitmapDescription() = BitmapDescriptor(gmsBitmapDescriptor = this)
