package com.rocket.android.core.data.map.model

import com.google.android.gms.maps.model.Polygon as GmsPolygon
import com.huawei.hms.maps.model.Polygon as HmsPolygon

/**
 * Class which represents a polygon on a map
 *
 * Contains an instance of [HmsPolygon]
 *
 * @property hmsPolygon instance of [HmsPolygon]
 */
class Polygon {
    var gmsPolygon: GmsPolygon? = null
    var hmsPolygon: HmsPolygon? = null

    /**
     * Removes [hmsPolygon] from the map
     */
    fun remove() {
        gmsPolygon?.remove()
        hmsPolygon?.remove()
    }
}

/**
 * Creates a [Polygon] from a [GmsPolygon]
 * @return a [Polygon] object with its [Polygon.gmsPolygon] as this polygon
 */
fun GmsPolygon.toPolygon() = Polygon().also { it.gmsPolygon = this }

/**
 * Creates a [Polygon] from a [HmsPolygon]
 * @return a [Polygon] object with its [Polygon.hmsPolygon] as this polygon
 */
fun HmsPolygon.toPolygon() = Polygon().also { it.hmsPolygon = this }
