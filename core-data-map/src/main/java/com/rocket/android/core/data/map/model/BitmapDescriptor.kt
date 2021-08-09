package com.rocket.android.core.data.map.model

import com.huawei.hms.maps.model.BitmapDescriptor as HmsBitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptor as GmsBitmapDescriptior


class BitmapDescriptor(
    val gmsBitmapDescriptor: GmsBitmapDescriptior? = null,
    val hmsBitmapDescriptor: HmsBitmapDescriptor? = null
)

fun GmsBitmapDescriptior.toBitmapDescription() = BitmapDescriptor(gmsBitmapDescriptor = this)
fun HmsBitmapDescriptor.toBitmapDescription() = BitmapDescriptor(hmsBitmapDescriptor = this)