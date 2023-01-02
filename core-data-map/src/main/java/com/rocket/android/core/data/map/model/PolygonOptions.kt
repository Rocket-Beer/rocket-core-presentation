package com.rocket.android.core.data.map.model

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.google.android.gms.maps.model.PolygonOptions as GmsPolygonOptions
import com.huawei.hms.maps.model.PolygonOptions as HmsPolygonOptions

/**
 * TODO
 *
 * TODO
 *
 * @property hmsPolygonOptions Instance of [HmsPolygonOptions], which defines options for a polygon
 */
class PolygonOptions(
    context: Context
) {
    var gmsPolygonOptions: GmsPolygonOptions? = null
        private set
    var hmsPolygonOptions: HmsPolygonOptions? = null
        private set

    init {
        when {
            HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(context) == com.huawei.hms.api.ConnectionResult.SUCCESS -> {
                if (isHmsCoreVersionAvailable(context = context)) {
                    hmsPolygonOptions = HmsPolygonOptions()
                } else {
                    gmsPolygonOptions = GmsPolygonOptions()
                }
            }
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS -> {

                gmsPolygonOptions = GmsPolygonOptions()
            }

            else -> {
                gmsPolygonOptions = GmsPolygonOptions()
            }
        }
    }

    /**
     * Adds vertices to the outline of [hmsPolygonOptions]
     * @param input list of [LatLng] objects to be used as vertices
     */
    fun add(input: List<LatLng>) {
        gmsPolygonOptions?.addAll(input.map { it.gmsLatLng })
        hmsPolygonOptions?.addAll(input.map { it.hmsLatLng })
    }

    /**
     * Adds a vertex to the outline of [hmsPolygonOptions]
     * @param point [LatLng] to be used as a vertex
     */
    fun add(point: LatLng) {
        gmsPolygonOptions?.add(point.gmsLatLng)
        hmsPolygonOptions?.add(point.hmsLatLng)
    }

    /**
     * Adds a hole to [hmsPolygonOptions]
     * @param input list of [LatLng] objects to be used as a hole
     */
    fun addHole(input: List<LatLng>) {
        gmsPolygonOptions?.addHole(input.map { it.gmsLatLng })
        hmsPolygonOptions?.addHole(input.map { it.hmsLatLng })
    }

    /**
     * Removes all the holes from [hmsPolygonOptions]
     */
    fun clearHoles() {
        gmsPolygonOptions?.holes?.clear()
        hmsPolygonOptions?.holes?.clear()
    }

    /**
     * Gets the holes for [hmsPolygonOptions]
     * @return the list of [List]<[LatLng]>s specifying the holes of [hmsPolygonOptions]
     */
    fun getHoles(): List<List<LatLng>>? {
        return gmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
            ?: hmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
    }

    /**
     * Gets the outline set for [hmsPolygonOptions]
     * @return the list of [LatLng]s specifying the vertices of [hmsPolygonOptions]
     */
    fun getPoints(): List<LatLng>? {
        return gmsPolygonOptions?.points?.map { it.toLatLng() }
            ?: hmsPolygonOptions?.points?.map { it.toLatLng() }
    }

    /**
     * Specifies [hmsPolygonOptions]' stroke width, in display pixels
     * @param stroke specified width for the stroke
     */
    fun strokeWidth(stroke: Float) {
        gmsPolygonOptions?.strokeWidth(stroke)
        hmsPolygonOptions?.strokeWidth(stroke)
    }

    /**
     * Specifies [hmsPolygonOptions]' fill color
     * @param color specified color for the fill
     */
    fun fillColor(color: Int) {
        gmsPolygonOptions?.fillColor(color)
        hmsPolygonOptions?.fillColor(color)
    }

    /**
     * Specifies [hmsPolygonOptions]' stroke color
     * @param color specified color for the stroke
     */
    fun strokeColor(color: Int) {
        gmsPolygonOptions?.strokeColor(color)
        hmsPolygonOptions?.strokeColor(color)
    }
}
