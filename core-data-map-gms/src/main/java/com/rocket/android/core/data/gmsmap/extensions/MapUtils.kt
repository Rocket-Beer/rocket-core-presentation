package com.rocket.android.core.data.gmsmap.extensions

import com.rocket.android.core.data.gmsmap.model.LatLng
import com.rocket.android.core.data.gmsmap.model.Marker
import com.rocket.android.core.data.gmsmap.model.Polygon
import com.google.android.gms.maps.model.LatLng as LatLngGMS
import com.google.android.gms.maps.model.Marker as MarkerGMS
import com.google.android.gms.maps.model.Polygon as GmsPolygon

fun MarkerGMS.toMarker() = Marker().also { it.gmsMarker = this }

fun GmsPolygon.toPolygon() = Polygon().also { it.gmsPolygon = this }

fun LatLngGMS.toLatLng() = LatLng(latitude = this.latitude, longitude = this.longitude)
