package com.rocket.android.core.data.map.model

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.huawei.hms.maps.model.PolygonOptions as HmsPolygonOptions
import com.rocket.android.core.data.gmsmap.model.PolygonOptions as GmsPolygonOptions

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
                    gmsPolygonOptions = GmsPolygonOptions(context = context)
                }
            }
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS -> {

                gmsPolygonOptions = GmsPolygonOptions(context = context)
            }

            else -> {
                gmsPolygonOptions = GmsPolygonOptions(context = context)
            }
        }
    }

    fun add(input: List<LatLng>) {
        gmsPolygonOptions?.add(input.map { it.toLatLngGMS() })
        hmsPolygonOptions?.addAll(input.map { it.hmsLatLng })
    }

    fun add(point: LatLng) {
        gmsPolygonOptions?.add(point.toLatLngGMS())
        hmsPolygonOptions?.add(point.hmsLatLng)
    }

    fun addHole(input: List<LatLng>) {
        gmsPolygonOptions?.addHole(input.map { it.toLatLngGMS() })
        hmsPolygonOptions?.addHole(input.map { it.hmsLatLng })
    }

    fun clearHoles() {
        gmsPolygonOptions?.clearHoles()
        hmsPolygonOptions?.holes?.clear()
    }

    fun getHoles(): List<List<LatLng>>? {
        return if (gmsPolygonOptions != null) {
            gmsPolygonOptions?.getHoles()?.map { hole -> hole.map { it.toLatLng() } }
        } else {
            hmsPolygonOptions?.holes?.map { hole -> hole.map { it.toLatLng() } }
        }
    }

    fun getPoints(): List<LatLng>? {
        return if (gmsPolygonOptions != null) {
            return gmsPolygonOptions?.getPoints()?.map { points -> points.toLatLng() }
        } else {
            hmsPolygonOptions?.points?.map { it.toLatLng() }
        }
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
