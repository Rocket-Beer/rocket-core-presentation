package com.rocket.android.core.data.gmsmap.model

import android.content.Context
import com.rocket.android.core.data.gmsmap.extensions.toLatLng
import com.google.android.gms.maps.model.PolygonOptions as GmsPolygonOptions

class PolygonOptions(
    context: Context
) {
    var gmsPolygonOptions: GmsPolygonOptions? = null
        private set

    init {
        gmsPolygonOptions = GmsPolygonOptions()
    }

    fun add(input: List<LatLng>) {
        gmsPolygonOptions?.addAll(input.map { it.gmsLatLng })
    }

    fun add(point: LatLng) {
        gmsPolygonOptions?.add(point.gmsLatLng)
    }

    fun addHole(input: List<LatLng>) {
        gmsPolygonOptions?.addHole(input.map { it.gmsLatLng })
    }

    fun clearHoles() {
        gmsPolygonOptions?.holes?.clear()
    }

    fun getHoles(): List<List<LatLng>>? {
        return gmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
    }

    fun getPoints(): List<LatLng>? {
        return gmsPolygonOptions?.points?.map { it.toLatLng() }
    }

    fun strokeWidth(stroke: Float) {
        gmsPolygonOptions?.strokeWidth(stroke)
    }

    fun fillColor(color: Int) {
        gmsPolygonOptions?.fillColor(color)
    }

    fun strokeColor(color: Int) {
        gmsPolygonOptions?.strokeColor(color)
    }
}
