package com.rocket.android.core.data.gmsmap.model

import android.content.Context
import com.rocket.android.core.data.gmsmap.extensions.toLatLng
import com.google.android.gms.maps.model.PolygonOptions as GmsPolygonOptions

/**
 * Class which holds some options applied to a polygon on a map, such as its points or holes
 *
 * Contains an instance of [GmsPolygonOptions] as well as methods to manage it
 *
 * @property gmsPolygonOptions instance of [GmsPolygonOptions], which defines options for a polygon
 */
class PolygonOptions(
    context: Context
) {
    var gmsPolygonOptions: GmsPolygonOptions? = null
        private set

    init {
        gmsPolygonOptions = GmsPolygonOptions()
    }

    /**
     * Adds vertices to the outline of [gmsPolygonOptions]
     * @param input list of [LatLng] objects to be used as vertices
     */
    fun add(input: List<LatLng>) {
        gmsPolygonOptions?.addAll(input.map { it.gmsLatLng })
    }

    /**
     * Adds a vertex to the outline of [gmsPolygonOptions]
     * @param point [LatLng] to be used as a vertex
     */
    fun add(point: LatLng) {
        gmsPolygonOptions?.add(point.gmsLatLng)
    }

    /**
     * Adds a hole to [gmsPolygonOptions]
     * @param input list of [LatLng] objects to be used as a hole
     */
    fun addHole(input: List<LatLng>) {
        gmsPolygonOptions?.addHole(input.map { it.gmsLatLng })
    }

    /**
     * Removes all the holes from [gmsPolygonOptions]
     */
    fun clearHoles() {
        gmsPolygonOptions?.holes?.clear()
    }

    /**
     * Gets the holes for [gmsPolygonOptions]
     * @return the list of [List]<[LatLng]>s specifying the holes of [gmsPolygonOptions]
     */
    fun getHoles(): List<List<LatLng>>? {
        return gmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
    }

    /**
     * Gets the outline set for [gmsPolygonOptions]
     * @return the list of [LatLng]s specifying the vertices of [gmsPolygonOptions]
     */
    fun getPoints(): List<LatLng>? {
        return gmsPolygonOptions?.points?.map { it.toLatLng() }
    }

    /**
     * Specifies [gmsPolygonOptions]' stroke width, in display pixels
     * @param stroke specified width for the stroke
     */
    fun strokeWidth(stroke: Float) {
        gmsPolygonOptions?.strokeWidth(stroke)
    }

    /**
     * Specifies [gmsPolygonOptions]' fill color
     * @param color specified color for the fill
     */
    fun fillColor(color: Int) {
        gmsPolygonOptions?.fillColor(color)
    }

    /**
     * Specifies [gmsPolygonOptions]' stroke color
     * @param color specified color for the stroke
     */
    fun strokeColor(color: Int) {
        gmsPolygonOptions?.strokeColor(color)
    }
}
