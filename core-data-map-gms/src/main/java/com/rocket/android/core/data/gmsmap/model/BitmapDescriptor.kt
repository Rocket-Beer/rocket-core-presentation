package com.rocket.android.core.data.gmsmap.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.BitmapDescriptor as GmsBitmapDescriptor

/**
 * Class which holds the definition of a [Bitmap] image
 *
 * Contains an instance of [GmsBitmapDescriptor]
 *
 * @property gmsBitmapDescriptor instance of [GmsBitmapDescriptor]
 */
class BitmapDescriptor(
    val gmsBitmapDescriptor: GmsBitmapDescriptor? = null
)

/**
 * Creates a [BitmapDescriptor] from the GMS bitmap descriptor
 * @return the [BitmapDescriptor] created
 */
fun GmsBitmapDescriptor.toBitmapDescription() = BitmapDescriptor(gmsBitmapDescriptor = this)
