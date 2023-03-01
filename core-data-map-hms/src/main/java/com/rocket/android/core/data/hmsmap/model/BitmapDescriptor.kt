package com.rocket.android.core.data.hmsmap.model

import android.graphics.Bitmap
import com.huawei.hms.maps.model.BitmapDescriptor as HmsBitmapDescriptor

/**
 * Class which holds the definition of a [Bitmap] image
 *
 * Contains an instance of [HmsBitmapDescriptor]
 *
 * @property hmsBitmapDescriptor instance of [HmsBitmapDescriptor]
 */
class BitmapDescriptor(
    val hmsBitmapDescriptor: HmsBitmapDescriptor? = null,
)
 /* Creates a [BitmapDescriptor] from the HMS bitmap descriptor
 * @return the [BitmapDescriptor] created
 */
fun HmsBitmapDescriptor.toBitmapDescription() = BitmapDescriptor(hmsBitmapDescriptor = this)
