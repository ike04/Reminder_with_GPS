package com.google.codelab.reminderwithgps.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputLayout
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.model.Remind
import com.google.codelab.reminderwithgps.utils.MapUtils
import com.google.codelab.reminderwithgps.utils.ValidationUtils
import io.realm.Realm
import java.util.*

class EditRemindFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener {
    private lateinit var mMap: GoogleMap
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1

    private lateinit var realm: Realm

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var locationCallback: LocationCallback? = null

    private var remindId: Long? = null
    private var editLatPin: Double = 0.0
    private var editLngPin: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.remind_list)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = Realm.getDefaultInstance()

        setFragmentResultListener("selected_remind") { _, bundle ->
            remindId = bundle.getLong("remind_id")
            view?.apply {
                findViewById<EditText>(R.id.editRemindTitleTextView)?.setText(bundle.getString("remind_title"))
                findViewById<TextInputLayout>(R.id.editRemindMemo).editText?.setText(
                    bundle.getString(
                        "remind_memo"
                    )
                )
                findViewById<CheckBox>(R.id.edit_isDone).isChecked =
                    bundle.getBoolean("remind_done")
            }
            editLatPin = bundle.getDouble("remind_lat")
            editLngPin = bundle.getDouble("remind_lng")
        }

        return inflater.inflate(R.layout.fragment_edit_remind, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.edit_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()
        addMarker(editLatPin, editLngPin)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(editLatPin, editLngPin), 14.0f))
        mMap.setOnMapLongClickListener(this)
    }

    private fun addMarker(lat: Double, lng: Double) {
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myLocationEnable()
        } else {
            MapUtils.requestLocationPermission(requireContext(), requireActivity())
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
                    Toast.makeText(context, "現在地は表示できません", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun myLocationEnable() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (locationResult?.lastLocation != null) {
                        lastLocation = locationResult.lastLocation
                        val currentLatLng = LatLng(editLatPin, editLngPin)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
                    }
                }
            }
        }
    }

    override fun onMapLongClick(point: LatLng) {
        mMap.clear()
        val markerOptions = MarkerOptions()
        val marker = markerOptions.position(point).title("リマインド箇所")
        mMap.addMarker(marker)
        editLatPin = markerOptions.position.latitude
        editLngPin = markerOptions.position.longitude
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_button -> {
                val title =
                    view?.findViewById<EditText>(R.id.editRemindTitleTextView)?.text.toString()
                val lat = editLatPin
                val lng = editLngPin
                val errorMessage = ValidationUtils.checkRemind(title, lat, lng)

                if (errorMessage == null) {
                    updateRemind(title, lat, lng)
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
                return true
            }
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun updateRemind(title: String, lat: Double, lng: Double) {
        val target = realm.where(Remind::class.java)
            .equalTo("id", remindId)
            .findFirst()

        realm.executeTransaction {
            target?.title = title
            target?.memo =
                view?.findViewById<TextInputLayout>(R.id.editRemindMemo)?.editText?.text.toString()
            target?.lat = lat
            target?.lng = lng
            target?.dateTime = Date()
            target?.isDone = view?.findViewById<CheckBox>(R.id.edit_isDone)?.isChecked == true
        }
    }
}
