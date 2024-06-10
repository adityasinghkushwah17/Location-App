package com.example.locationapp

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.health.connect.datatypes.ExerciseRoute.Location
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context:Context) {
private val _fusedLocationClient:FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(context)


@SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        val locationCallback=object :LocationCallback(){
            override fun onLocationResult(locationresult: LocationResult) {
                super.onLocationResult(locationresult)
                locationresult.lastLocation?.let {
                    val location=LocationData(Latitude = it.latitude, Longitude = it.longitude)
                    viewModel.updatelocation(location)
                }
            }
        }
        val locationrequest=LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,1000).build()
    _fusedLocationClient.requestLocationUpdates(
        locationrequest,locationCallback, Looper.getMainLooper())
    }

    fun hasLocationPermission(context: Context):Boolean{

        return ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED

    }
    fun reversegeocodeLocation(location: LocationData):String{

        val geocoder=Geocoder(context, Locale.getDefault())
        val coordinate=LatLng(location.Latitude,location.Longitude)
        val addresses:MutableList<Address>? = geocoder.getFromLocation(coordinate.latitude,
            coordinate.longitude,1)

        return if(addresses?.isNotEmpty()==true){
            addresses[0].getAddressLine(0)
        }else{
            "Address Not Found"
        }

    }

}

