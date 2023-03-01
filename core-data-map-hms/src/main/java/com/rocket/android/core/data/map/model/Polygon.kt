package com.rocket.android.core.data.map.model

import com.huawei.hms.maps.model.Polygon as HmsPolygon

/**
 * Class which represents a polygon on a map
 *
 * Contains an instance of [HmsPolygon]
 *
 * @property hmsPolygon instance of [HmsPolygon]
 */
class Polygon {
    var hmsPolygon: HmsPolygon? = null

    /**
     * Removes [hmsPolygon] from the map
     */
    fun remove() {
        hmsPolygon?.remove()
    }
}

/**
 * Creates a [Polygon] from a [HmsPolygon]
 * @return a [Polygon] object with its [Polygon.hmsPolygon] as this polygon
 */
fun HmsPolygon.toPolygon() = Polygon().also { it.hmsPolygon = this }
