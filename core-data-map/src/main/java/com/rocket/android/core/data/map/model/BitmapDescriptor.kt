package com.rocket.android.core.data.map.model

import com.huawei.hms.maps.model.BitmapDescriptor as HmsBitmapDescriptor
import com.rocket.android.core.data.gmsmap.model.BitmapDescriptor as GmsBitmapDescriptor

class BitmapDescriptor(
    val gmsBitmapDescriptor: GmsBitmapDescriptor? = null,
    val hmsBitmapDescriptor: HmsBitmapDescriptor? = null
)

fun HmsBitmapDescriptor.toHMSBitmapDescription() = BitmapDescriptor(hmsBitmapDescriptor = this)
fun GmsBitmapDescriptor.toGMSBitmapDescription() = BitmapDescriptor(gmsBitmapDescriptor = this)
