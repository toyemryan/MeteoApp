import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import android.provider.Settings
import android.util.Log
import com.example.meteoapp.R

class LocationPermission(private val activity: Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(callback: (Boolean) -> Unit) {
        if (isLocationPermissionGranted()) {
            // La permission de localisation est déjà accordée
            callback(true)
        } else {
            // Demande la permission de localisation
            ActivityCompat.requestPermissions(
                activity as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        callback: (Boolean) -> Unit
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("LocationPermission", R.string.location_good.toString())
                    callback(true)
                } else {
                    Log.e("LocationPermission", R.string.location_bad.toString())
                    callback(false)
                }
            }
        }
    }

    fun showLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(locationListener: (Location) -> Unit) {
        if (isLocationPermissionGranted()) {
            val locationRequest = LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000) // Intervalle de mise à jour de la localisation en millisecondes

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                       // if (activity != null){
                            locationListener.invoke(location)
                            Log.d("LocationPermission", "Location updated: $location")
                      //  }
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            Log.d("LocationPermission", R.string.access_location.toString())
        }
    }
}
