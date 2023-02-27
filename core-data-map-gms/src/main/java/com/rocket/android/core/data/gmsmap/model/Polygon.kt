package com.rocket.android.core.data.gmsmap.model

import com.google.android.gms.maps.model.Polygon as GmsPolygon

/**
 * Class which represents a polygon on a map
 *
 * Contains an instance of [GmsPolygon]
 *
 * @property gmsPolygon instance of [GmsPolygon]
 */
class Polygon {
    var gmsPolygon: GmsPolygon? = null

    /**
     * Removes [gmsPolygon] from the map
     */
    fun remove() {
        gmsPolygon?.remove()
    }
}
