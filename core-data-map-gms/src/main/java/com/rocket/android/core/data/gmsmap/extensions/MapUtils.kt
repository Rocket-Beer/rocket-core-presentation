package com.rocket.android.core.data.gmsmap.extensions

import com.rocket.android.core.data.gmsmap.model.LatLng
import com.rocket.android.core.data.gmsmap.model.Marker
import com.rocket.android.core.data.gmsmap.model.Polygon
import com.google.android.gms.maps.model.LatLng as LatLngGMS
import com.google.android.gms.maps.model.Marker as MarkerGMS
import com.google.android.gms.maps.model.Polygon as GmsPolygon

/**
 * Creates a [Marker] from this marker
 * @return a [Marker] object with this marker as its [Marker.gmsMarker]
 */
fun MarkerGMS.toMarker() = Marker().also { it.gmsMarker = this }

/**
 * Creates a [Polygon] from a [GmsPolygon]
 * @return a [Polygon] object with its [Polygon.gmsPolygon] as this polygon
 */
fun GmsPolygon.toPolygon() = Polygon().also { it.gmsPolygon = this }


/**
 * Creates a [LatLng] from a [LatLngGMS]
 * @return the [LatLng] created
 */
fun LatLngGMS.toLatLng() = LatLng(latitude = this.latitude, longitude = this.longitude)
