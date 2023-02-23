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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMapOptions
import com.rocket.android.core.data.map.extensions.isHmsCoreVersionAvailable
import com.rocket.android.core.data.map.model.*
import com.google.android.gms.common.ConnectionResult as ConnectionResultGMS
import com.google.android.gms.maps.MapView as GmsMapView
import com.google.android.gms.maps.model.CircleOptions as CircleOptionsGMS
import com.google.android.gms.maps.model.MapStyleOptions as GmsMapStyleOptions
import com.google.android.gms.maps.model.Marker as GmsMarker
import com.huawei.hms.api.ConnectionResult as ConnectionResultHMS
import com.huawei.hms.maps.MapView as HmsMapView
import com.huawei.hms.maps.model.CircleOptions as CircleOptionsHMS
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

    /**
     * Calls [onCreate] method on [hmsMapView] passing [bundle] as a parameter
     */
    fun onCreate(bundle: Bundle?) {
        gmsMapView?.onCreate(bundle)
        hmsMapView?.onCreate(bundle)
    }

    /**
     * Calls [onStart] method on [hmsMapView]
     */
    fun onStart() {
        gmsMapView?.onStart()
        hmsMapView?.onStart()
    }

    /**
     * Calls [onResume] method on [hmsMapView]
     */
    fun onResume() {
        gmsMapView?.onResume()
        hmsMapView?.onResume()
    }

    /**
     * Calls [onPause] method on [hmsMapView]
     */
    fun onPause() {
        gmsMapView?.onPause()
        hmsMapView?.onPause()
    }

    /**
     * Calls [onStop] method on [hmsMapView]
     */
    fun onStop() {
        gmsMapView?.onStop()
        hmsMapView?.onStop()
    }

    /**
     * Calls [onDestroy] method on [hmsMapView]
     */
    fun onDestroy() {
        gmsMapView?.onDestroy()
        hmsMapView?.onDestroy()
    }

    /**
     * Calls [onLowMemory] method on [hmsMapView]
     */
    fun onLowMemory() {
        gmsMapView?.onLowMemory()
        hmsMapView?.onLowMemory()
    }

    /**
     * Stores [hmsMapView]'s state inside [bundle] before getting destroyed
     */
    fun onSaveInstanceState(bundle: Bundle) {
        gmsMapView?.onSaveInstanceState(bundle)
        hmsMapView?.onSaveInstanceState(bundle)
    }

    /**
     * Used to acquire the map for [hmsMapView] and then call [mapReady] when it is ready
     */
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

    /**
     * Sets the styling of [hmsMap] with the options provided with [style]
     */
    fun setStyle(@RawRes style: Int) {
        gmsMap?.setMapStyle(GmsMapStyleOptions.loadRawResourceStyle(context, style))
        hmsMap?.setMapStyle(HmsMapStyleOptions.loadRawResourceStyle(context, style))
    }

    /**
     * Sets the preference for whether [hmsMap]'s Toolbar should be enabled or disabled, indicated by [enabled]
     */
    fun mapToolbarEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMapToolbarEnabled = enabled
        hmsMap?.uiSettings?.isMapToolbarEnabled = enabled
    }

    /**
     * Sets the preference for whether all gestures should be enabled or disabled on [hmsMap], indicated by [enabled]
     */
    fun allGesturesEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
        hmsMap?.uiSettings?.setAllGesturesEnabled(enabled)
    }

    /**
     * Sets the preference for whether my-location button should be enabled or disabled, indicated by [enabled]
     */
    fun myLocationButtonEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
        hmsMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    /**
     * Sets the preference for whether the compass should be enabled or disabled, indicated by [enabled]
     */
    fun compassEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isCompassEnabled = enabled
        hmsMap?.uiSettings?.isCompassEnabled = enabled
    }

    /**
     * Sets the preference for whether the zoom controls should be enabled or disabled, indicated by [enabled]
     */
    fun zoomControlsEnabled(enabled: Boolean) {
        gmsMap?.uiSettings?.isZoomControlsEnabled = enabled
        hmsMap?.uiSettings?.isZoomControlsEnabled = enabled
    }

    /**
     * Removes all markers, polylines, polygons, overlays, etc from [hmsMap], as well as the markers inside
     * [hmsMarkersList]
     */
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

    /**
     * Sets the preference for whether my-location layer should be enabled or disabled, indicated by [enabled]
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun myLocationEnabled(enabled: Boolean) {
        gmsMap?.isMyLocationEnabled = enabled
        hmsMap?.isMyLocationEnabled = enabled
    }

    /**
     * Gets the status of my-location layer
     * @return true if the my-location layer is enabled; false otherwise
     */
    fun isMyLocationEnabled(): Boolean? {
        return gmsMap?.isMyLocationEnabled ?: hmsMap?.isMyLocationEnabled
    }

    /**
     * Specifies whether [hmsMap] should be created in lite mode, indicated by [enabled]
     */
    fun liteMode(enabled: Boolean) {
        gmsMap?.mapType = GoogleMapOptions().liteMode(enabled).mapType
        hmsMap?.mapType = HuaweiMapOptions().liteMode(enabled).mapType
    }

    /**
     * Sets the action to be performed (contained in [onClick]) when clicking on [hmsMap]
     */
    fun setOnMapClickListener(onClick: () -> Unit) {
        gmsMap?.setOnMapClickListener {
            onClick()
        }

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

    /**
     * Adds a [List] of markers to [hmsMap] and [hmsMarkersList]
     * @param markers [MarkerOptions]'s [List] which defines how to render each of the markers
     */
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
            } else if (hmsMap != null) {
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

    /**
     * Removes all the markers from [hmsMap] and [hmsMarkersList] whose ids are contained inside [list]
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

    /**
     * Sets the action to be performed (contained in [clickListener]) when clicking on a marker located in [hmsMap]
     */
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

    /**
     * Adds a polygon to [hmsMap] and returns it afterwards. Might be null if there's an error while performing this
     * operation
     * @param polygon [PolygonOptions] object which defines how to render the polygon
     */
    fun addPolygon(polygon: PolygonOptions): Polygon? {
        if (gmsMap != null) {
            return gmsMap?.addPolygon(polygon.gmsPolygonOptions)?.toPolygon()
        }

        if (hmsMap != null) {
            return hmsMap?.addPolygon(polygon.hmsPolygonOptions)?.toPolygon()
        }

        return null
    }

    /**
     * Sets the action to be performed (contained in [onIdleListener]) when [hmsMap]'s camera is idle
     */
    fun onCameraIdle(onIdleListener: () -> Unit) {
        gmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }

        hmsMap?.setOnCameraIdleListener {
            onIdleListener()
        }
    }

    /**
     * Gets [hmsMap]'s camera zoom level near the center of the screen
     */
    fun getCameraZoom(): Float? {
        return gmsMap?.cameraPosition?.zoom ?: hmsMap?.cameraPosition?.zoom
    }

    /**
     * Gets the location that [hmsMap]'s camera is pointing at
     */
    fun getCameraTarget(): LatLng? {
        return gmsMap?.cameraPosition?.target?.toLatLng()
            ?: hmsMap?.cameraPosition?.target?.toLatLng()
    }

    /**
     * Sets [hmsMap]'s camera location to [position] and sets its zoom level to [zoom]
     * @param animateDuration [hmsMap]'s camera movement duration in milliseconds
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

        gmsMap?.let { map ->
            val circleOptions = CircleOptionsGMS()
                .center(customCircleOptions.center.gmsLatLng)
                .radius(customCircleOptions.radius)
                .strokeColor(customCircleOptions.strokeColor)
                .strokeWidth(customCircleOptions.strokeWidth)
                .fillColor(customCircleOptions.fillColor)
                .zIndex(customCircleOptions.zIndex)
                .visible(customCircleOptions.visible)

            map.addCircle(circleOptions)
        }

        hmsMap?.let { map ->
            val circleOptions = CircleOptionsHMS()
                .center(customCircleOptions.center.hmsLatLng)
                .radius(customCircleOptions.radius)
                .strokeColor(customCircleOptions.strokeColor)
                .strokeWidth(customCircleOptions.strokeWidth)
                .fillColor(customCircleOptions.fillColor)
                .zIndex(customCircleOptions.zIndex)
                .visible(customCircleOptions.visible)
            map.addCircle(circleOptions)
        }
    }
}
