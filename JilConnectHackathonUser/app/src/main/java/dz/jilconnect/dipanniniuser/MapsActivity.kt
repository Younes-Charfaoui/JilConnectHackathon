package dz.jilconnect.dipanniniuser

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val currentLocationMarkers = mutableMapOf<String, Marker>()
    private val markersColors = listOf(
        HUE_AZURE, HUE_BLUE, HUE_CYAN,
        HUE_GREEN, HUE_MAGENTA, HUE_ORANGE,
        HUE_RED, HUE_ROSE, HUE_VIOLET, HUE_YELLOW
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 55)
        }

        needFab.setOnClickListener {
            Log.d("TAGME", "Start of the way.")
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location != null)
                        CoroutineScope(Dispatchers.IO).launch {
                            val networkLayer = NetworkLayer()
                            val response = networkLayer.getWorkerData(
                                "${location.latitude},${location.longitude}"
                            )
                            Log.d("TAGME", response.toString())
                            CoroutineScope(Dispatchers.Main).launch {
                                for (result in response) {
                                    showMarker(result)
                                }
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Problem of location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMyLocationButtonEnabled = true

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location != null)
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 15.0f
                        )
                    )
            }
        } else {
            Toast.makeText(this, "Please accept permission", Toast.LENGTH_LONG).show()
        }

        map.setInfoWindowAdapter(WorkerMarkerInfoAdapter(this))
    }

    private fun showMarker(result: WorkersResult) {
        val locations = result.location.split(",")
        val latLng = LatLng(locations[0].toDouble(), locations[1].toDouble())
        if (!currentLocationMarkers.containsKey(result.id.toString())) {
            currentLocationMarkers[result.id.toString()] =
                map.addMarker(
                    MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker())
                        .position(latLng)
                        .title(result.name)
                        .snippet("Phone : ${result.phone} \nDistance : ${result.distance}")
                        .icon(BitmapDescriptorFactory.defaultMarker(markersColors.shuffled()[0]))
                )
            currentLocationMarkers[result.id.toString()]?.tag = result
            currentLocationMarkers[result.id.toString()]?.showInfoWindow()
        }
    }
}
