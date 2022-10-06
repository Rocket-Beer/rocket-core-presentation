package com.rocket.android.core.data.gmsmap

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RawRes
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.rocket.android.core.data.gmsmap.extensions.toLatLng
import com.rocket.android.core.data.gmsmap.extensions.toMarker
import com.rocket.android.core.data.gmsmap.extensions.toPolygon
import com.rocket.android.core.data.gmsmap.model.*
import com.google.android.gms.maps.MapView as GmsMapView
import com.google.android.gms.maps.model.MapStyleOptions as GmsMapStyleOptions
import com.google.android.gms.maps.model.Marker as GmsMarker

class CoreGmsMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var gmsMapView: GmsMapView? = null
    private var gmsMap: GoogleMap? = null
    private var gmsMarkersList = mutableListOf<GmsMarker>()

    init {
        gmsMapView = findViewById(R.id.gms_map_view) ?: View.inflate(
            context,
            R.layout.core_map_gms,
            this@CoreGmsMapView
        ).findViewById(R.id.gms_map_view)
    }

    fun onCreate(bundle: Bundle?) {
        gmsMapView?.onCreate(bundle)
    }

    fun onStart() {
        gmsMapView?.onStart()
    }

    fun onResume() {
        gmsMapView?.onResume()
    }

    fun onPause() {
        gmsMapView?.onPause()
    }

    fun onStop() {
        gmsMapView?.onStop()
    }

    fun onDestroy() {
        gmsMapView?.onDestroy()
    }

    fun onLowMemory() {
        gmsMapView?.onLowMemory()
    }

    fun onSaveInstanceState(bundle: Bundle) {
        gmsMapView?.onSaveInstanceState(bundle)
    }

    fun getMapAsync(mapReady: () -> Unit) {
        gmsMapView?.getMapAsync { map ->
            gmsMap = map
            mapReady()
        }
    }

    fun setStyle(@RawRes style: Int) {
        gmsMap?.setMapStyle(GmsMapStyleOptions.loadRawResourceStyle(context, style))
    }

    fun mapToolbarEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMapToolbarEnabled = enabled
    }

    fun allGesturesEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
    }

    fun myLocationButtonEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    fun compassEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isCompassEnabled = enabled
    }

    fun zoomControlsEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isZoomControlsEnabled = enabled
    }

    fun clear() {
        gmsMap?.let { map ->
            gmsMarkersList.clear()
            map.clear()
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun myLocationEnabled(enabled: Boolean) {
        gmsMap?.isMyLocationEnabled = enabled
    }

    fun isMyLocationEnabled(): Boolean? {
        return gmsMap?.isMyLocationEnabled
    }

    fun liteMode(enabled: Boolean) {
        gmsMap?.mapType = GoogleMapOptions().liteMode(enabled).mapType
    }

    fun setOnMapClickListener(onClick: () -> Unit) {
        gmsMap?.setOnMapClickListener {
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

        return null
    }

    fun setMarkers(markers: List<MarkerOptions>) {
        markers.forEach { markerOption ->
            gmsMap?.addMarker(markerOption.gmsMarkerOptions)?.also { marker ->
                markerOption.id = marker.id
                gmsMarkersList.add(marker)
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
            }
        }
    }

    fun onMarkerClickListener(clickListener: ((Marker) -> Unit)) {
        gmsMap?.setOnMarkerClickListener { marker ->
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
    }

    fun addPolygon(polygon: PolygonOptions): Polygon? {
        if (gmsMap != null) {
            return gmsMap?.addPolygon(polygon.gmsPolygonOptions)?.toPolygon()
        }

        return null
    }

    fun onCameraIdle(onIdleListener: () -> Unit) {
        gmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }
    }

    fun getCameraZoom(): Float? {
        return gmsMap?.cameraPosition?.zoom
    }

    fun getCameraTarget(): LatLng? {
        return gmsMap?.cameraPosition?.target?.toLatLng()
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
    }

    fun drawCircle(customCircleOptions: CustomCircleOptions) {
        val circleOptions = CircleOptions()
            .center(customCircleOptions.center)
            .radius(customCircleOptions.radius)
            .strokeColor(customCircleOptions.strokeColor)
            .strokeWidth(customCircleOptions.strokeWidth)
            .fillColor(customCircleOptions.fillColor)

        gmsMap?.addCircle(circleOptions)
    }
}
