package com.rocket.android.core.data.gmsmap.model

import com.google.android.gms.maps.model.BitmapDescriptor as GmsBitmapDescriptior

class BitmapDescriptor(
    val gmsBitmapDescriptor: GmsBitmapDescriptior? = null,
)

fun GmsBitmapDescriptior.toBitmapDescription() = BitmapDescriptor(gmsBitmapDescriptor = this)
