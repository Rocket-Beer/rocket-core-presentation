package com.rocket.android.core.data.map

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RawRes
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintLayout
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMapOptions
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.rocket.android.core.data.map.model.*
import com.rocket.android.core.data.map.model.Marker
import com.huawei.hms.maps.MapView as HmsMapView
import com.huawei.hms.maps.model.MapStyleOptions as HmsMapStyleOptions
import com.huawei.hms.maps.model.Marker as HmsMarker

/**
 * Class which represents a map
 *
 * Contains all the necessary information to show a map and manage its camera, markers, polygons and behaviour
 *
 * @property hmsMapView instance of [HmsMapView], which is a view that displays a map with data obtained from Huawei
 * Maps Service
 * @property hmsMap instance of [HuaweiMap], which holds all methods related to the map
 * @property hmsMarkersList [HmsMarker]'s [List]. They represent icons placed at a particular point on the map's surface
 * @param context [Context] object with the current context for the layout
 * @param attributeSet [AttributeSet] object to be passed to the layout
 * @param defStyleAttr style to be applied to the layout
 */
class CoreMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var hmsMapView: HmsMapView? = null
    private var hmsMap: HuaweiMap? = null
    private var hmsMarkersList = mutableListOf<HmsMarker>()

    init {
        if (isHmsCoreVersionAvailable(context = context)) {
            hmsMapView = findViewById(R.id.hms_map_view) ?: View.inflate(
                context,
                R.layout.core_map_hms,
                this@CoreMapView
            ).findViewById(R.id.hms_map_view)
        }
    }

    /**
     * Calls [onCreate] method on [hmsMapView] passing [bundle] as a parameter
     */
    fun onCreate(bundle: Bundle?) {
        hmsMapView?.onCreate(bundle)
    }

    /**
     * Calls [onStart] method on [hmsMapView]
     */
    fun onStart() {
        hmsMapView?.onStart()
    }

    /**
     * Calls [onResume] method on [hmsMapView]
     */
    fun onResume() {
        hmsMapView?.onResume()
    }

    /**
     * Calls [onPause] method on [hmsMapView]
     */
    fun onPause() {
        hmsMapView?.onPause()
    }

    /**
     * Calls [onStop] method on [hmsMapView]
     */
    fun onStop() {
        hmsMapView?.onStop()
    }

    /**
     * Calls [onDestroy] method on [hmsMapView]
     */
    fun onDestroy() {
        hmsMapView?.onDestroy()
    }

    /**
     * Calls [onLowMemory] method on [hmsMapView]
     */
    fun onLowMemory() {
        hmsMapView?.onLowMemory()
    }

    /**
     * Stores [hmsMapView]'s state inside [bundle] before getting destroyed
     */
    fun onSaveInstanceState(bundle: Bundle) {
        hmsMapView?.onSaveInstanceState(bundle)
    }

    /**
     * Used to acquire the map for [hmsMapView] and then call [mapReady] when it is ready
     */
    fun getMapAsync(mapReady: () -> Unit) {
        hmsMapView?.getMapAsync { map ->
            hmsMap = map
            mapReady()
        }
    }

    /**
     * Sets the styling of [hmsMap] with the options provided with [style]
     */
    fun setStyle(@RawRes style: Int) {
        hmsMap?.setMapStyle(HmsMapStyleOptions.loadRawResourceStyle(context, style))
    }

    /**
     * Sets the preference for whether [hmsMap]'s Toolbar should be enabled or disabled, indicated by [enabled]
     */
    fun mapToolbarEnabled(enabled: Boolean) {
        hmsMap?.uiSettings?.isMapToolbarEnabled = enabled
    }

    /**
     * Sets the preference for whether all gestures should be enabled or disabled on [hmsMap], indicated by [enabled]
     */
    fun allGesturesEnabled(enabled: Boolean) {
        hmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
    }

    /**
     * Sets the preference for whether my-location button should be enabled or disabled, indicated by [enabled]
     */
    fun myLocationButtonEnabled(enabled: Boolean) {
        hmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    /**
     * Sets the preference for whether the compass should be enabled or disabled, indicated by [enabled]
     */
    fun compassEnabled(enabled: Boolean) {
        hmsMap?.uiSettings?.isCompassEnabled = enabled
    }

    /**
     * Sets the preference for whether the zoom controls should be enabled or disabled, indicated by [enabled]
     */
    fun zoomControlsEnabled(enabled: Boolean) {
        hmsMap?.uiSettings?.isZoomControlsEnabled = enabled
    }

    /**
     * Removes all markers, polylines, polygons, overlays, etc from [hmsMap], as well as the markers inside
     * [hmsMarkersList]
     */
    fun clear() {
        hmsMap?.let { map ->
            hmsMarkersList.clear()
            map.clear()
        }
    }

    /**
     * Sets the preference for whether my-location layer should be enabled or disabled, indicated by [enabled]
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun myLocationEnabled(enabled: Boolean) {
        hmsMap?.isMyLocationEnabled = enabled
    }

    /**
     * Gets the status of my-location layer
     * @return true if the my-location layer is enabled; false otherwise
     */
    fun isMyLocationEnabled()
            : Boolean
    ? {
        return hmsMap?.isMyLocationEnabled
    }

    /**
     * Specifies whether [hmsMap] should be created in lite mode, indicated by [enabled]
     */
    fun liteMode(enabled: Boolean) {
        hmsMap?.mapType = HuaweiMapOptions().liteMode(enabled).mapType
    }

    /**
     * Sets the action to be performed (contained in [onClick]) when clicking on [hmsMap]
     */
    fun setOnMapClickListener(
        onClick: ()
        -> Unit
    ) {
        hmsMap?.setOnMapClickListener {
            onClick()
        }
    }

    /**
     * Adds a marker to [hmsMap] and [hmsMarkersList]
     * @param markerOptions [MarkerOptions] object which defines how to render the marker
     * @return the [Marker] that was added. Might be null if there's an error while performing this operation
     */
    fun addMarker(markerOptions: MarkerOptions): Marker? {
        if (hmsMap != null) {
            return hmsMap?.addMarker(markerOptions.hmsMarkerOptions)?.also { marker ->
                markerOptions.id = marker.id
                hmsMarkersList.add(marker)
            }?.toMarker()
        }
        return null
    }

    /**
     * Adds a [List] of markers to [hmsMap] and [hmsMarkersList]
     * @param markers [MarkerOptions]'s [List] which defines how to render each of the markers
     */
    fun setMarkers(
        markers: List<MarkerOptions>
    ) {
        markers.forEach { markerOption ->
            hmsMap?.addMarker(markerOption.hmsMarkerOptions)?.also { marker ->
                markerOption.id = marker.id
                hmsMarkersList.add(marker)
            }
        }
    }

    /**
     * Sets the visibility to true of all the markers whose ids are contained inside [list]
     */
    fun showMarkersI(list: List<MarkerOptions>) {
        list.forEach { markerOptions ->
            if (hmsMap != null) {
                hmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }.forEach { marker ->
                    marker.isVisible = true
                }
            }
        }
    }

    /**
     * Sets the visibility to false of all the markers whose ids are contained inside [list]
     */
    fun hideMarkersI(
        list: List<MarkerOptions>
    ) {
        list.forEach { markerOptions ->
            if (hmsMap != null) {
                hmsMarkersList.filter { marker ->
                    markerOptions.id == marker.id
                }.forEach { marker ->
                    marker.isVisible = false
                }
            }
        }
    }

    /**
     * Removes all the markers from [hmsMap] and [hmsMarkersList] whose ids are contained inside [list]
     */
    fun clearMarkersI(list: List<MarkerOptions>) {
        list.forEach { markerOptions ->
            if (hmsMap != null) {
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

    /**
     * Sets the action to be performed (contained in [clickListener]) when clicking on a marker located in [hmsMap]
     */
    fun onMarkerClickListener(
        clickListener: ((Marker)
        -> Unit
        )
    ) {

        hmsMap?.setOnMarkerClickListener { marker ->
            clickListener(marker.toMarker())
            true
        }
    }

    /**
     * Sets the actions to be performed when dragging a marker located in [hmsMap]
     * @param dragStartListener Called when a marker starts being dragged
     * @param dragListener Called repeatedly while a marker is being dragged
     * @param dragEndListener Called when a marker has finished being dragged
     */
    fun onMarkerDragListener(
        dragStartListener: ((Marker) -> Unit),
        dragListener: ((Marker) -> Unit),
        dragEndListener: ((Marker) -> Unit),
    ) {
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

    /**
     * Adds a polygon to [hmsMap] and returns it afterwards. Might be null if there's an error while performing this
     * operation
     * @param polygon [PolygonOptions] object which defines how to render the polygon
     */
    fun addPolygon(polygon: PolygonOptions)
            : Polygon
    ? {
        if (hmsMap != null) {
            return hmsMap?.addPolygon(polygon.hmsPolygonOptions)?.toPolygon()
        }

        return null
    }

    /**
     * Sets the action to be performed (contained in [onIdleListener]) when [hmsMap]'s camera is idle
     */
    fun onCameraIdle(onIdleListener: () -> Unit) {
        hmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }
    }

    /**
     * Gets [hmsMap]'s camera zoom level near the center of the screen
     */
    fun getCameraZoom()
            : Float
    ? {
        return hmsMap?.cameraPosition?.zoom
    }

    /**
     * Gets the location that [hmsMap]'s camera is pointing at
     */
    fun getCameraTarget(): LatLng? {
        return hmsMap?.cameraPosition?.target?.toLatLng()
    }

    /**
     * Sets [hmsMap]'s camera location to [position] and sets its zoom level to [zoom]
     * @param animateDuration [hmsMap]'s camera movement duration in milliseconds
     */
    fun setCameraPosition(position: LatLng, zoom: Float, animateDuration: Int) {
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
