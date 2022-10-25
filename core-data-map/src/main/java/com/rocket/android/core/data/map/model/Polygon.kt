package com.rocket.android.core.data.map.model

import com.huawei.hms.maps.model.Polygon as HmsPolygon
import com.rocket.android.core.data.gmsmap.model.Polygon as GmsPolygon

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
