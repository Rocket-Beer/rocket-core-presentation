package com.rocket.android.core.data.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RawRes
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.pm.PackageInfoCompat
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMapOptions
import com.rocket.android.core.data.map.model.*
import java.lang.Exception
import com.google.android.gms.common.ConnectionResult as ConnectionResultGMS
import com.google.android.gms.maps.MapView as GmsMapView
import com.google.android.gms.maps.model.MapStyleOptions as GmsMapStyleOptions
import com.google.android.gms.maps.model.Marker as GmsMarker
import com.huawei.hms.api.ConnectionResult as ConnectionResultHMS
import com.huawei.hms.maps.MapView as HmsMapView
import com.huawei.hms.maps.model.MapStyleOptions as HmsMapStyleOptions
import com.huawei.hms.maps.model.Marker as HmsMarker

class CoreMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var gmsMapView: GmsMapView? = null
    private var gmsMap: GoogleMap? = null
    private var gmsMarkersList = mutableListOf<GmsMarker>()

    private var hmsMapView: HmsMapView? = null
    private var hmsMap: HuaweiMap? = null
    private var hmsMarkersList = mutableListOf<HmsMarker>()

    init {
        when {
            HuaweiApiAvailability.getInstance()
                .isHuaweiMobileServicesAvailable(context) == ConnectionResultHMS.SUCCESS -> {
                if (isHmsCoreVersionAvailable()) {
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

    private fun isHmsCoreVersionAvailable(): Boolean{
        return try {
            val pm: PackageManager = context.packageManager
            val packageInfo: PackageInfo = pm.getPackageInfo("com.huawei.hwid", 0)
            val version : Long = PackageInfoCompat.getLongVersionCode(packageInfo)
            version >= 50000301L
        } catch (e: Exception) {
            false
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
            gmsMap = map
            mapReady()
        }

        hmsMapView?.getMapAsync { map ->
            hmsMap = map
            mapReady()
        }
    }

    fun setStyle(@RawRes style: Int) {
        gmsMap?.setMapStyle(GmsMapStyleOptions.loadRawResourceStyle(context, style))
        hmsMap?.setMapStyle(HmsMapStyleOptions.loadRawResourceStyle(context, style))
    }

    fun mapToolbarEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMapToolbarEnabled = enabled
        hmsMap?.uiSettings?.isMapToolbarEnabled = enabled
    }

    fun allGesturesEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
        hmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
    }

    fun myLocationButtonEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
        hmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    fun compassEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isCompassEnabled = enabled
        hmsMap?.uiSettings?.isCompassEnabled = enabled
    }

    fun zoomControlsEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isZoomControlsEnabled = enabled
        hmsMap?.uiSettings?.isZoomControlsEnabled = enabled
    }

    fun clear() {
        gmsMap?.let { map ->
            gmsMarkersList.clear()
            map.clear()
        }

        hmsMap?.let { map ->
            hmsMarkersList.clear()
            map.clear()
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun myLocationEnabled(enabled: Boolean) {
        gmsMap?.isMyLocationEnabled = enabled
        hmsMap?.isMyLocationEnabled = enabled
    }

    fun isMyLocationEnabled(): Boolean? {
        return gmsMap?.isMyLocationEnabled ?: hmsMap?.isMyLocationEnabled
    }

    fun liteMode(enabled: Boolean) {
        gmsMap?.mapType = GoogleMapOptions().liteMode(enabled).mapType
        hmsMap?.mapType = HuaweiMapOptions().liteMode(enabled).mapType
    }

    fun setOnMapClickListener(onClick: () -> Unit) {
        gmsMap?.setOnMapClickListener {
            onClick()
        }

        hmsMap?.setOnMapClickListener {
            onClick()
        }
    }

    fun addMarker(markerOptions: MarkerOptions): Marker? {
        if (gmsMap != null) {
            return gmsMap?.addMarker(markerOptions.gmsMarkerOptions)?.also { marker ->
                markerOptions.id = marker.id
                gmsMarkersList.add(marker)
            }?.toMarker()
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
            gmsMap?.addMarker(markerOption.gmsMarkerOptions)?.also { marker ->
                markerOption.id = marker.id
                gmsMarkersList.add(marker)
            }

            hmsMap?.addMarker(markerOption.hmsMarkerOptions)?.also { marker ->
                markerOption.id = marker.id
                hmsMarkersList.add(marker)
            }
        }
    }

    fun showMarkersI(list: List<MarkerOptions>) {
        list.forEach { markerOptions ->
            if (gmsMap != null) {
                gmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }.forEach { marker ->
                    marker.isVisible = true
                }
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
            if (gmsMap != null) {
                gmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }.forEach { marker ->
                    marker.isVisible = false
                }
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
            if (gmsMap != null) {
                val filteredMarkerList = gmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }
                filteredMarkerList.forEach { marker ->
                    marker.remove()
                }
                gmsMarkersList.removeAll(filteredMarkerList)
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
        gmsMap?.setOnMarkerClickListener { marker ->
            clickListener(marker.toMarker())
            true
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
        gmsMap?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: GmsMarker) {
                dragStartListener(marker.toMarker())
            }

            override fun onMarkerDrag(marker: GmsMarker) {
                dragListener(marker.toMarker())
            }

            override fun onMarkerDragEnd(marker: GmsMarker) {
                dragEndListener(marker.toMarker())
            }
        })

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
        if (gmsMap != null) {
            return gmsMap?.addPolygon(polygon.gmsPolygonOptions)?.toPolygon()
        }

        if (hmsMap != null) {
            return hmsMap?.addPolygon(polygon.hmsPolygonOptions)?.toPolygon()
        }

        return null
    }

    fun onCameraIdle(onIdleListener: () -> Unit) {
        gmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }

        hmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }
    }

    fun getCameraZoom(): Float? {
        return gmsMap?.cameraPosition?.zoom ?: hmsMap?.cameraPosition?.zoom
    }

    fun getCameraTarget(): LatLng? {
        return gmsMap?.cameraPosition?.target?.toLatLng()
            ?: hmsMap?.cameraPosition?.target?.toLatLng()
    }

    fun setCameraPosition(position: LatLng, zoom: Float, animateDuration: Int) {
        gmsMap?.let { map ->
            val gmsCameraPosition = CameraPosition.builder()
            gmsCameraPosition.target(position.gmsLatLng)
            gmsCameraPosition.zoom(zoom)

            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(gmsCameraPosition.build()),
                animateDuration,
                null
            )
        }

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
}
