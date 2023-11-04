import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import android.location.Location
import android.provider.Settings
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest



class LocationPermission(private val activity: Activity){

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(callback: (Boolean) -> Unit) {
        if (isLocationPermissionGranted()) {
            // La permission de localisation est déjà accordée
            callback(true)
        } else {
            // Demander la permission de localisation
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Ajouter cette constante pour identifier la demande de permission dans onRequestPermissionsResult
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        callback: (Boolean) -> Unit
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // La permission de localisation a été accordée
                callback(true)
            } else {
                // La permission de localisation a été refusée
                callback(false)
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
                .setInterval(10000) // Intervalle de mise à jour de la localisation en millisecondes

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        locationListener.invoke(location)
                    }
                }

            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }


}
