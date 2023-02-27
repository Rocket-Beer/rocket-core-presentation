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
import com.rocket.android.core.data.gmsmap.extensions.toLatLng
import com.rocket.android.core.data.gmsmap.extensions.toMarker
import com.rocket.android.core.data.gmsmap.extensions.toPolygon
import com.rocket.android.core.data.gmsmap.model.LatLng
import com.rocket.android.core.data.gmsmap.model.Marker
import com.rocket.android.core.data.gmsmap.model.MarkerOptions
import com.rocket.android.core.data.gmsmap.model.Polygon
import com.rocket.android.core.data.gmsmap.model.PolygonOptions
import com.google.android.gms.maps.MapView as GmsMapView
import com.google.android.gms.maps.model.MapStyleOptions as GmsMapStyleOptions
import com.google.android.gms.maps.model.Marker as GmsMarker

/**
 * Class which represents a map
 *
 * Contains all the necessary information to show a map and manage its camera, markers, polygons and behaviour
 *
 * @property gmsMapView instance of [GmsMapView], which is a view that displays a map with data obtained from Google
 * Maps Service
 * @property gmsMap instance of [GoogleMap], which holds all methods related to the map
 * @property gmsMarkersList [GmsMarker]'s [List]. They represent icons placed at a particular point on the map's surface
 * @param context [Context] object with the current context for the layout
 * @param attributeSet [AttributeSet] object to be passed to the layout
 * @param defStyleAttr style to be applied to the layout
 */
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

    /**
     * Calls [onCreate] method on [gmsMapView] passing [bundle] as a parameter
     */
    fun onCreate(bundle: Bundle?) {
        gmsMapView?.onCreate(bundle)
    }

    /**
     * Calls [onStart] method on [gmsMapView]
     */
    fun onStart() {
        gmsMapView?.onStart()
    }

    /**
     * Calls [onResume] method on [gmsMapView]
     */
    fun onResume() {
        gmsMapView?.onResume()
    }

    /**
     * Calls [onPause] method on [gmsMapView]
     */
    fun onPause() {
        gmsMapView?.onPause()
    }

    /**
     * Calls [onStop] method on [gmsMapView]
     */
    fun onStop() {
        gmsMapView?.onStop()
    }

    /**
     * Calls [onDestroy] method on [gmsMapView]
     */
    fun onDestroy() {
        gmsMapView?.onDestroy()
    }

    /**
     * Calls [onLowMemory] method on [gmsMapView]
     */
    fun onLowMemory() {
        gmsMapView?.onLowMemory()
    }

    /**
     * Stores [gmsMapView]'s state inside [bundle] before getting destroyed
     */
    fun onSaveInstanceState(bundle: Bundle) {
        gmsMapView?.onSaveInstanceState(bundle)
    }

    /**
     * Used to acquire the map for [gmsMapView] and then call [mapReady] when it is ready
     */
    fun getMapAsync(mapReady: () -> Unit) {
        gmsMapView?.getMapAsync { map ->
            gmsMap = map
            mapReady()
        }
    }

    /**
     * Sets the styling of [gmsMap] with the options provided with [style]
     */
    fun setStyle(@RawRes style: Int) {
        gmsMap?.setMapStyle(GmsMapStyleOptions.loadRawResourceStyle(context, style))
    }

    /**
     * Sets the preference for whether [gmsMap]'s Toolbar should be enabled or disabled, indicated by [enabled]
     */
    fun mapToolbarEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMapToolbarEnabled = enabled
    }

    /**
     * Sets the preference for whether all gestures should be enabled or disabled on [gmsMap], indicated by [enabled]
     */
    fun allGesturesEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
    }

    /**
     * Sets the preference for whether my-location button should be enabled or disabled, indicated by [enabled]
     */
    fun myLocationButtonEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    /**
     * Sets the preference for whether the compass should be enabled or disabled, indicated by [enabled]
     */
    fun compassEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isCompassEnabled = enabled
    }

    /**
     * Sets the preference for whether the zoom controls should be enabled or disabled, indicated by [enabled]
     */
    fun zoomControlsEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isZoomControlsEnabled = enabled
    }

    /**
     * Removes all markers, polylines, polygons, overlays, etc from [gmsMap], as well as the markers inside
     * [gmsMarkersList]
     */
    fun clear() {
        gmsMap?.let { map ->
            gmsMarkersList.clear()
            map.clear()
        }
    }

    /**
     * Sets the preference for whether my-location layer should be enabled or disabled, indicated by [enabled]
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun myLocationEnabled(enabled: Boolean) {
        gmsMap?.isMyLocationEnabled = enabled
    }

    /**
     * Gets the status of my-location layer
     * @return true if the my-location layer is enabled; false otherwise
     */
    fun isMyLocationEnabled(): Boolean? {
        return gmsMap?.isMyLocationEnabled
    }

    /**
     * Specifies whether [gmsMap] should be created in lite mode, indicated by [enabled]
     */
    fun liteMode(enabled: Boolean) {
        gmsMap?.mapType = GoogleMapOptions().liteMode(enabled).mapType
    }

    /**
     * Sets the action to be performed (contained in [onClick]) when clicking on [gmsMap]
     */
    fun setOnMapClickListener(onClick: () -> Unit) {
        gmsMap?.setOnMapClickListener {
            onClick()
        }
    }

    /**
     * Adds a marker to [gmsMap] and [gmsMarkersList]
     * @param markerOptions [MarkerOptions] object which defines how to render the marker
     * @return the [Marker] that was added. Might be null if there's an error while performing this operation
     */
    fun addMarker(markerOptions: MarkerOptions): Marker? {
        if (gmsMap != null) {
            return gmsMap?.addMarker(markerOptions.gmsMarkerOptions)?.also { marker ->
                markerOptions.id = marker.id
                gmsMarkersList.add(marker)
            }?.toMarker()
        }

        return null
    }

    /**
     * Adds a [List] of markers to [gmsMap] and [gmsMarkersList]
     * @param markers [MarkerOptions]'s [List] which defines how to render each of the markers
     */
    fun setMarkers(markers: List<MarkerOptions>) {
        markers.forEach { markerOption ->
            gmsMap?.addMarker(markerOption.gmsMarkerOptions)?.also { marker ->
                markerOption.id = marker.id
                gmsMarkersList.add(marker)
            }
        }
    }

    /**
     * Sets the visibility to true of all the markers whose ids are contained inside [list]
     */
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

    /**
     * Sets the visibility to false of all the markers whose ids are contained inside [list]
     */
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

    /**
     * Removes all the markers from [gmsMap] and [gmsMarkersList] whose ids are contained inside [list]
     */
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

    /**
     * Sets the action to be performed (contained in [clickListener]) when clicking on a marker located in [gmsMap]
     */
    fun onMarkerClickListener(clickListener: ((Marker) -> Unit)) {
        gmsMap?.setOnMarkerClickListener { marker ->
            clickListener(marker.toMarker())
            true
        }
    }

    /**
     * Sets the actions to be performed when dragging a marker located in [gmsMap]
     * @param dragStartListener Called when a marker starts being dragged
     * @param dragListener Called repeatedly while a marker is being dragged
     * @param dragEndListener Called when a marker has finished being dragged
     */
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

    /**
     * Adds a polygon to [gmsMap] and returns it afterwards. Might be null if there's an error while performing this
     * operation
     * @param polygon [PolygonOptions] object which defines how to render the polygon
     */
    fun addPolygon(polygon: PolygonOptions): Polygon? {
        if (gmsMap != null) {
            return gmsMap?.addPolygon(polygon.gmsPolygonOptions)?.toPolygon()
        }

        return null
    }

    /**
     * Sets the action to be performed (contained in [onIdleListener]) when [gmsMap]'s camera is idle
     */
    fun onCameraIdle(onIdleListener: () -> Unit) {
        gmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }
    }

    /**
     * Gets [gmsMap]'s camera zoom level near the center of the screen
     */
    fun getCameraZoom(): Float? {
        return gmsMap?.cameraPosition?.zoom
    }

    /**
     * Gets the location that [gmsMap]'s camera is pointing at
     */
    fun getCameraTarget(): LatLng? {
        return gmsMap?.cameraPosition?.target?.toLatLng()
    }

    /**
     * Sets [gmsMap]'s camera location to [position] and sets its zoom level to [zoom]
     * @param animateDuration [gmsMap]'s camera movement duration in milliseconds
     */
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
}
