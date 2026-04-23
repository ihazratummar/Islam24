package com.hazrat.qibla.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.hazrat.ui.R
import androidx.core.graphics.scale
import com.google.maps.android.compose.MapsComposeExperimentalApi


/**
 * @author Hazrat Ummar Shaikh
 * Created on 25-01-2025
 */


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun QiblaMapView(
    modifier: Modifier = Modifier,
    qiblaLocation: LatLng,
    latitude: Double,
    longitude: Double
) {

    val context = LocalContext.current
    val contextResource = LocalResources.current

    val cameraPositionState = rememberCameraPositionState()

    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        zoomGesturesEnabled = false,
        scrollGesturesEnabled = false,
        tiltGesturesEnabled = false,
        rotationGesturesEnabled = false,
        mapToolbarEnabled = false
        )

    val myLocation = remember(latitude, longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            LatLng(latitude, longitude)
        } else null
    }
    Log.d("QiblaMapViewDebug", "Latitude: $latitude, Longitude: $longitude")

    if (myLocation == null) {
        Log.d("QiblaMapViewDebug", "Waiting for valid location...")
        return
    }

    GoogleMap(
        modifier = modifier,
        properties = MapProperties(),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings
    ) {

        MapEffect(Unit) { map ->
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_json))
            val bearing  = SphericalUtil.computeHeading(myLocation, qiblaLocation)

            Log.d("cameraPositionState", "camera bearing $bearing")
            myLocation.let { location ->
                val currentIcon: Bitmap = BitmapFactory.decodeResource(contextResource, R.drawable.current_location_mark)
                    .scale(100, 100, false)
                Log.d("QiblaMapView2", "QiblaMapView: ${location.latitude} ${location.longitude}")
                map.addMarker(
                    MarkerOptions()
                .position(location)
                .title("Current Location").icon(BitmapDescriptorFactory.fromBitmap(currentIcon)).rotation(bearing.toFloat())
                )
            }

            val qiblaIcon: Bitmap =
                BitmapFactory.decodeResource(contextResource, R.drawable.kaabaniddle)
                    .scale(80, 80, false)

            map.addMarker(
                MarkerOptions()
            .position(qiblaLocation)
            .title("Qibla").icon(BitmapDescriptorFactory.fromBitmap(qiblaIcon))
            )

            val boundBuilder = LatLngBounds.Builder()
            myLocation.let { boundBuilder.include(it) }
            boundBuilder.include(qiblaLocation)

            val bounds = boundBuilder.build()

            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))

            val path = listOf(
                myLocation,
                qiblaLocation
            )
            val polylineOptions = PolylineOptions()
                .addAll(path)
                .geodesic(true)
                .width(15f)
                .color(Color.CYAN)
                .pattern(
                    listOf(
                        Dash(30f),
                        Gap(20f)
                    )
                )

            map.addPolyline(polylineOptions)
        }
    }
}