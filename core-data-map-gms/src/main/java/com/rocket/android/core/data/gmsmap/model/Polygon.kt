package com.rocket.android.core.data.gmsmap.model

import com.google.android.gms.maps.model.Polygon as GmsPolygon

class Polygon {
    var gmsPolygon: GmsPolygon? = null

    fun remove() {
        gmsPolygon?.remove()
    }
}
