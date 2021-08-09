package com.rocket.android.core.data.map.model

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.google.android.gms.maps.model.PolygonOptions as GmsPolygonOptions
import com.huawei.hms.maps.model.PolygonOptions as HmsPolygonOptions

class PolygonOptions(
    context: Context
) {
    var gmsPolygonOptions: GmsPolygonOptions? = null

        private set
    var hmsPolygonOptions: HmsPolygonOptions? = null
        private set

    init {
        when {
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS -> {

                gmsPolygonOptions = GmsPolygonOptions()
            }
            HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(context) == com.huawei.hms.api.ConnectionResult.SUCCESS -> {

                hmsPolygonOptions = HmsPolygonOptions()
            }
            else -> {
                gmsPolygonOptions = GmsPolygonOptions()
            }
        }
    }

    fun add(input: List<LatLng>) {
        gmsPolygonOptions?.addAll(input.map { it.gmsLatLng })
        hmsPolygonOptions?.addAll(input.map { it.hmsLatLng })
    }

    fun add(point: LatLng) {
        gmsPolygonOptions?.add(point.gmsLatLng)
        hmsPolygonOptions?.add(point.hmsLatLng)
    }

    fun addHole(input: List<LatLng>) {
        gmsPolygonOptions?.addHole(input.map { it.gmsLatLng })
        hmsPolygonOptions?.addHole(input.map { it.hmsLatLng })
    }

    fun clearHoles() {
        gmsPolygonOptions?.holes?.clear()
        hmsPolygonOptions?.holes?.clear()
    }

    fun getHoles(): List<List<LatLng>>? {
        return gmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
            ?: hmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }

    }

    fun getPoints(): List<LatLng>? {
        return gmsPolygonOptions?.points?.map { it.toLatLng() }
            ?: hmsPolygonOptions?.points?.map { it.toLatLng() }
    }

    fun strokeWidth(stroke: Float) {
        gmsPolygonOptions?.strokeWidth(stroke)
        hmsPolygonOptions?.strokeWidth(stroke)
    }

    fun fillColor(color: Int) {
        gmsPolygonOptions?.fillColor(color)
        hmsPolygonOptions?.fillColor(color)
    }

    fun strokeColor(color: Int) {
        gmsPolygonOptions?.strokeColor(color)
        hmsPolygonOptions?.strokeColor(color)
    }
}