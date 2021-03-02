package com.google.codelab.reminderwithgps.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.model.Remind
import com.google.codelab.reminderwithgps.utils.MapUtils.requestLocationPermission
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*

class AddRemindActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var realm: Realm
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var locationCallback: LocationCallback? = null
    private var selectedLat: Double = 0.0
    private var selectedLng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remind)

        realm = Realm.getDefaultInstance()

        supportActionBar?.setTitle(R.string.add_remind)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()
        mMap.setOnMapLongClickListener(this)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myLocationEnable()
        } else {
            requestLocationPermission(this, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 許可された
                    myLocationEnable()
                } else {
                    Toast.makeText(this, "現在地は表示できません", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun myLocationEnable() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            val locationRequest = LocationRequest().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (locationResult?.lastLocation != null) {
                        lastLocation = locationResult.lastLocation
                        val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    override fun onMapLongClick(point: LatLng) {
        mMap.clear()
        val markerOptions = MarkerOptions()
        val marker = markerOptions.position(point).title("リマインド箇所")
        mMap.addMarker(marker)
        selectedLat = markerOptions.position.latitude
        selectedLng = markerOptions.position.longitude
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_button -> {
                createRemind()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun createRemind() {
        val maxId = realm.where<Remind>().max("id")
        val nextId = (maxId?.toLong() ?: 0L) + 1L

        realm.executeTransaction {
            val remind = realm.createObject(Remind::class.java, nextId)

            remind.title =
                findViewById<EditText>(R.id.addRemindTitleTextView).text.toString()
            remind.lat = selectedLat
            remind.lng = selectedLng
            remind.dateTime = Date()
            remind.isDone = false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focus = currentFocus ?: return false

        imm.hideSoftInputFromWindow(
            focus.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        return false
    }
}
