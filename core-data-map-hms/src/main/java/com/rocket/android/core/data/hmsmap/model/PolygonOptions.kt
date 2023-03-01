package com.rocket.android.core.data.hmsmap.model

import android.content.Context
import com.rocket.android.core.data.hmsmap.extensions.isHmsCoreVersionAvailable
import com.huawei.hms.maps.model.PolygonOptions as HmsPolygonOptions

/**
 * Class which holds some options applied to a polygon on a map, such as its points or holes
 *
 * Contains an instance of [HmsPolygonOptions] as well as methods to manage it
 *
 * @property hmsPolygonOptions instance of [HmsPolygonOptions], which defines options for a polygon
 */
class PolygonOptions(
    context: Context,
) {
    var hmsPolygonOptions: HmsPolygonOptions? = null
        private set

    init {
        if (isHmsCoreVersionAvailable(context = context)) {
            hmsPolygonOptions = HmsPolygonOptions()
        }
    }

    /**
     * Adds vertices to the outline of [hmsPolygonOptions]
     * @param input list of [LatLng] objects to be used as vertices
     */
    fun add(input: List<LatLng>) {
        hmsPolygonOptions?.addAll(input.map { it.hmsLatLng })
    }

    /**
     * Adds a vertex to the outline of [hmsPolygonOptions]
     * @param point [LatLng] to be used as a vertex
     */
    fun add(point: LatLng) {
        hmsPolygonOptions?.add(point.hmsLatLng)
    }

    /**
     * Adds a hole to [hmsPolygonOptions]
     * @param input list of [LatLng] objects to be used as a hole
     */
    fun addHole(input: List<LatLng>) {
        hmsPolygonOptions?.addHole(input.map { it.hmsLatLng })
    }

    /**
     * Removes all the holes from [hmsPolygonOptions]
     */
    fun clearHoles() {
        hmsPolygonOptions?.holes?.clear()
    }

    /**
     * Gets the holes for [hmsPolygonOptions]
     * @return the list of [List]<[LatLng]>s specifying the holes of [hmsPolygonOptions]
     */
    fun getHoles(): List<List<LatLng>>? {
        return hmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
    }

    /**
     * Gets the outline set for [hmsPolygonOptions]
     * @return the list of [LatLng]s specifying the vertices of [hmsPolygonOptions]
     */
    fun getPoints(): List<LatLng>? {
        return hmsPolygonOptions?.points?.map { it.toLatLng() }
    }

    /**
     * Specifies [hmsPolygonOptions]' stroke width, in display pixels
     * @param stroke specified width for the stroke
     */
    fun strokeWidth(stroke: Float) {
        hmsPolygonOptions?.strokeWidth(stroke)
    }

    /**
     * Specifies [hmsPolygonOptions]' fill color
     * @param color specified color for the fill
     */
    fun fillColor(color: Int) {
        hmsPolygonOptions?.fillColor(color)
    }

    /**
     * Specifies [hmsPolygonOptions]' stroke color
     * @param color specified color for the stroke
     */
    fun strokeColor(color: Int) {
        hmsPolygonOptions?.strokeColor(color)
    }
}
