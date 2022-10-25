package com.rocket.android.core.data.map

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RawRes
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMapOptions
import com.rocket.android.core.data.gmsmap.CoreGmsMapView
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.rocket.android.core.data.map.model.CustomCircleOptions
import com.rocket.android.core.data.map.model.LatLng
import com.rocket.android.core.data.map.model.Marker
import com.rocket.android.core.data.map.model.MarkerOptions
import com.rocket.android.core.data.map.model.Polygon
import com.rocket.android.core.data.map.model.PolygonOptions
import com.rocket.android.core.data.map.model.toCustomCircleOptionsGMS
import com.rocket.android.core.data.map.model.toLatLng
import com.rocket.android.core.data.map.model.toLatLngGMS
import com.rocket.android.core.data.map.model.toMarker
import com.rocket.android.core.data.map.model.toPolygon
import com.google.android.gms.common.ConnectionResult as ConnectionResultGMS
import com.huawei.hms.api.ConnectionResult as ConnectionResultHMS
import com.huawei.hms.maps.MapView as HmsMapView
import com.huawei.hms.maps.model.MapStyleOptions as HmsMapStyleOptions
import com.huawei.hms.maps.model.Marker as HmsMarker

class CoreMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var gmsMapView: CoreGmsMapView? = null
    private var hmsMapView: HmsMapView? = null
    private var hmsMap: HuaweiMap? = null
    private var hmsMarkersList = mutableListOf<HmsMarker>()

    init {
        when {
            HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(context) == ConnectionResultHMS.SUCCESS -> {
                if (isHmsCoreVersionAvailable(context = context)) {
                    hmsMapView = findViewById(R.id.hms_map_view) ?: View.inflate(
                        context,
                        R.layout.core_map_hms,
                        this@CoreMapView
                    ).findViewById(R.id.hms_map_view)
                } else {
                    gmsMapView = findViewById(R.id.gms_map_view) ?: View.inflate(
                        context,
                        R.layout.core_map_gms,
                        this@CoreMapView
                    ).findViewById(R.id.gms_map_view)
                }
            }
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResultGMS.SUCCESS -> {

                gmsMapView = findViewById(R.id.gms_map_view) ?: View.inflate(
                    context,
                    R.layout.core_map_gms,
                    this@CoreMapView
                ).findViewById(R.id.gms_map_view)
            }
            else -> {
                gmsMapView = findViewById(R.id.gms_map_view) ?: View.inflate(
                    context,
                    R.layout.core_map_gms,
                    this@CoreMapView
                ).findViewById(R.id.gms_map_view)
            }
        }
    }

    fun onCreate(bundle: Bundle?) {
        gmsMapView?.onCreate(bundle)
        hmsMapView?.onCreate(bundle)
    }

    fun onStart() {
        gmsMapView?.onStart()
        hmsMapView?.onStart()
    }

    fun onResume() {
        gmsMapView?.onResume()
        hmsMapView?.onResume()
    }

    fun onPause() {
        gmsMapView?.onPause()
        hmsMapView?.onPause()
    }

    fun onStop() {
        gmsMapView?.onStop()
        hmsMapView?.onStop()
    }

    fun onDestroy() {
        gmsMapView?.onDestroy()
        hmsMapView?.onDestroy()
    }

    fun onLowMemory() {
        gmsMapView?.onLowMemory()
        hmsMapView?.onLowMemory()
    }

    fun onSaveInstanceState(bundle: Bundle) {
        gmsMapView?.onSaveInstanceState(bundle)
        hmsMapView?.onSaveInstanceState(bundle)
    }

    fun getMapAsync(mapReady: () -> Unit) {
        gmsMapView?.getMapAsync { map ->
            map
            mapReady()
        }

        hmsMapView?.getMapAsync { map ->
            hmsMap = map
            mapReady()
        }
    }

    fun setStyle(@RawRes style: Int) {
        gmsMapView?.setStyle(style)
        hmsMap?.setMapStyle(HmsMapStyleOptions.loadRawResourceStyle(context, style))
    }

    fun mapToolbarEnabled(enabled: Boolean) {
        gmsMapView?.mapToolbarEnabled(enabled = enabled)
        hmsMap?.uiSettings?.isMapToolbarEnabled = enabled
    }

    fun allGesturesEnabled(enabled: Boolean) {
        gmsMapView?.allGesturesEnabled(enabled = enabled)
        hmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
    }

    fun myLocationButtonEnabled(enabled: Boolean) {
        gmsMapView?.myLocationButtonEnabled(enabled = enabled)
        hmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    fun compassEnabled(enabled: Boolean) {
        gmsMapView?.compassEnabled(enabled = enabled)
        hmsMap?.uiSettings?.isCompassEnabled = enabled
    }

    fun zoomControlsEnabled(enabled: Boolean) {
        gmsMapView?.zoomControlsEnabled(enabled = enabled)
        hmsMap?.uiSettings?.isZoomControlsEnabled = enabled
    }

    fun clear() {
        gmsMapView?.clear()
        hmsMap?.let { map ->
            hmsMarkersList.clear()
            map.clear()
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun myLocationEnabled(enabled: Boolean) {
        gmsMapView?.myLocationEnabled(enabled = enabled)
        hmsMap?.isMyLocationEnabled = enabled
    }

    fun isMyLocationEnabled(): Boolean? {
        return gmsMapView?.isMyLocationEnabled()
            ?: hmsMap?.isMyLocationEnabled
    }

    fun liteMode(enabled: Boolean) {
        gmsMapView?.liteMode(enabled = enabled)
        hmsMap?.mapType = HuaweiMapOptions().liteMode(enabled).mapType
    }

    fun setOnMapClickListener(onClick: () -> Unit) {
        gmsMapView?.setOnMapClickListener {
            onClick()
        }

        hmsMap?.setOnMapClickListener {
            onClick()
        }
    }

    fun addMarker(markerOptions: MarkerOptions): Marker? {
        if (gmsMapView != null) {
            gmsMapView?.addMarker(markerOptions = markerOptions.gmsMarkerOptions!!)
        }

        if (hmsMap != null) {
            return hmsMap?.addMarker(markerOptions.hmsMarkerOptions)?.also { marker ->
                markerOptions.id = marker.id
                hmsMarkersList.add(marker)
            }?.toMarker()
        }

        return null
    }

    fun setMarkers(markers: List<MarkerOptions>) {
        markers.forEach { markerOption ->
            gmsMapView?.setMarkers(markers = markers.map { marker -> marker.gmsMarkerOptions!! })

            hmsMap?.addMarker(markerOption.hmsMarkerOptions)?.also { marker ->
                markerOption.id = marker.id
                hmsMarkersList.add(marker)
            }
        }
    }

    fun showMarkersI(list: List<MarkerOptions>) {
        list.forEach { markerOptions ->
            if (gmsMapView != null) {
                gmsMapView?.showMarkersI(list = list.map { markerOption -> markerOption.gmsMarkerOptions!! })
            } else if (hmsMap != null) {
                hmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }.forEach { marker ->
                    marker.isVisible = true
                }
            }
        }
    }

    fun hideMarkersI(list: List<MarkerOptions>) {
        list.forEach { markerOptions ->
            if (gmsMapView != null) {
                gmsMapView?.hideMarkersI(list = list.map { markerOption -> markerOption.gmsMarkerOptions!! })
            } else if (hmsMap != null) {
                hmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }.forEach { marker ->
                    marker.isVisible = false
                }
            }
        }
    }

    fun clearMarkersI(list: List<MarkerOptions>) {
        list.forEach { markerOptions ->
            if (gmsMapView != null) {
                gmsMapView?.clearMarkersI(list = list.map { markerOption -> markerOption.gmsMarkerOptions!! })
            } else if (hmsMap != null) {
                val filteredMarkerList = hmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }
                filteredMarkerList.forEach { marker ->
                    marker.remove()
                }
                hmsMarkersList.removeAll(filteredMarkerList)
            }
        }
    }

    fun onMarkerClickListener(clickListener: ((Marker) -> Unit)) {
        gmsMapView?.onMarkerClickListener {
            clickListener.invoke(it.toMarker())
        }

        hmsMap?.setOnMarkerClickListener { marker ->
            clickListener(marker.toMarker())
            true
        }
    }

    fun onMarkerDragListener(
        dragStartListener: ((Marker) -> Unit),
        dragListener: ((Marker) -> Unit),
        dragEndListener: ((Marker) -> Unit)
    ) {
        gmsMapView?.onMarkerDragListener(
            dragStartListener = { dragStartListener.invoke(it.toMarker()) },
            dragListener = { dragListener.invoke(it.toMarker()) },
            dragEndListener = { dragEndListener.invoke(it.toMarker()) }
        )

        hmsMap?.setOnMarkerDragListener(object : HuaweiMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: HmsMarker) {
                dragStartListener(marker.toMarker())
            }

            override fun onMarkerDrag(marker: HmsMarker) {
                dragListener(marker.toMarker())
            }

            override fun onMarkerDragEnd(marker: HmsMarker) {
                dragEndListener(marker.toMarker())
            }
        })
    }

    fun addPolygon(polygon: PolygonOptions): Polygon? {
        if (gmsMapView != null) {
            gmsMapView?.addPolygon(polygon = polygon.gmsPolygonOptions!!)
        }

        if (hmsMap != null) {
            return hmsMap?.addPolygon(polygon.hmsPolygonOptions)?.toPolygon()
        }

        return null
    }

    fun onCameraIdle(onIdleListener: () -> Unit) {
        gmsMapView?.onCameraIdle(onIdleListener = onIdleListener)

        hmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }
    }

    fun getCameraZoom(): Float? {
        return gmsMapView?.getCameraZoom()
            ?: hmsMap?.cameraPosition?.zoom
    }

    fun getCameraTarget(): LatLng? {
        return gmsMapView?.getCameraTarget()?.toLatLng()
            ?: hmsMap?.cameraPosition?.target?.toLatLng()
    }

    fun setCameraPosition(position: LatLng, zoom: Float, animateDuration: Int) {
        gmsMapView?.setCameraPosition(
            position = position.toLatLngGMS(),
            zoom = zoom,
            animateDuration = animateDuration
        )

        hmsMap?.let { map ->
            val hmsCameraPosition = com.huawei.hms.maps.model.CameraPosition.builder()
            hmsCameraPosition.target(position.hmsLatLng)
            hmsCameraPosition.zoom(zoom)

            map.animateCamera(
                com.huawei.hms.maps.CameraUpdateFactory.newCameraPosition(hmsCameraPosition.build()),
                animateDuration,
                null
            )
        }
    }

    fun drawCircle(customCircleOptions: CustomCircleOptions) {

        gmsMapView?.drawCircle(customCircleOptions.toCustomCircleOptionsGMS())

        hmsMap?.addCircle(customCircleOptions.circleOptionsHMS)
    }
}
