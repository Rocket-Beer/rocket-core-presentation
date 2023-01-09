package com.rocket.android.core.data.map.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.BitmapDescriptor as GmsBitmapDescriptior
import com.huawei.hms.maps.model.BitmapDescriptor as HmsBitmapDescriptor

/**
 * Class which holds the definition of a [Bitmap] image
 *
 * Contains an instance of [HmsBitmapDescriptor]
 *
 * @property hmsBitmapDescriptor instance of [HmsBitmapDescriptor]
 */
class BitmapDescriptor(
    val gmsBitmapDescriptor: GmsBitmapDescriptior? = null,
    val hmsBitmapDescriptor: HmsBitmapDescriptor? = null,
)

fun GmsBitmapDescriptior.toBitmapDescription() = BitmapDescriptor(gmsBitmapDescriptor = this)

/**
 * Creates a [BitmapDescriptor] from the HMS bitmap descriptor
 * @return the [BitmapDescriptor] created
 */
fun HmsBitmapDescriptor.toBitmapDescription() = BitmapDescriptor(hmsBitmapDescriptor = this)
