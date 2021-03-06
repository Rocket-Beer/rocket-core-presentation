package com.rocket.android.core.data.map.model

import com.google.android.gms.maps.model.Polygon as GmsPolygon
import com.huawei.hms.maps.model.Polygon as HmsPolygon

class Polygon {
    var gmsPolygon: GmsPolygon? = null
    var hmsPolygon: HmsPolygon? = null

    fun remove() {
        gmsPolygon?.remove()
        hmsPolygon?.remove()
    }
}

fun GmsPolygon.toPolygon() = Polygon().also { it.gmsPolygon = this }

fun HmsPolygon.toPolygon() = Polygon().also { it.hmsPolygon = this }
