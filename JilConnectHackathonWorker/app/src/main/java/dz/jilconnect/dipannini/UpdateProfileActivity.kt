package dz.jilconnect.dipannini

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dz.jilconnect.dipannini.webservices.NetworkLayer
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private lateinit var locationCallback: LocationCallback
    private var currentLocation = ""
    private val locationRequest = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 55)
        }

        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                if (locationResult == null) {

                    return
                }
                for (location in locationResult.locations) {
                    currentLocation = "${location.latitude} , ${location.longitude}"
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 55)
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

        myLocationFab.setOnClickListener {

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Please Activate Location", Toast.LENGTH_LONG).show()
                turnOnGpsDialog()
                return@setOnClickListener
            }

            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 55)
                return@setOnClickListener
            } else {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    location = it.result
                }
            }
        }

        saveButton.setOnClickListener {
            if (location == null) {
                Toast.makeText(
                    this,
                    "Please retry getting your location",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val networkLayer = NetworkLayer()
                CoroutineScope(Dispatchers.IO).launch {
                    networkLayer.updateUserData(
                        PreferencesManager(this@UpdateProfileActivity).userId,
                        phoneEdit.text.toString(),
                        "${location!!.longitude} , ${location!!.latitude}"
                    )
                    finish()
                }
            }
        }
    }

    private fun turnOnGpsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GPS Disactivated")
        builder.setMessage("You have to enable GPS to get your position.")
        builder.setCancelable(false).setPositiveButton("Yes") { _, _ ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.cancel()
        }.show()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 55)
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
