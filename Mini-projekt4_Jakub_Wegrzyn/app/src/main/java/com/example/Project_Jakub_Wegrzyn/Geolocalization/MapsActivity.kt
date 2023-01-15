package com.example.Project_Jakub_Wegrzyn.Geolocalization

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.PlacesGeo
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.UserGeoViewModel
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.databinding.ActivityMapsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val userGeoViewModel by viewModels<UserGeoViewModel>()
    private var arrayListOfLocalizations = ArrayList<PlacesGeo>()
    private lateinit var geoClient: GeofencingClient

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        userGeoViewModel.localizations.observe(this, Observer {
                userGeo ->
            userGeo?.let {
                arrayListOfLocalizations.addAll(it)

            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        runOnUiThread {
            Handler().postDelayed({
                mMap = googleMap

                for(item in arrayListOfLocalizations){
                    if(item!= null){
                        markerOptions(item)
                        geofence(item)

                    }
                }

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                mMap.isMyLocationEnabled = true

            },5100L)
        }
    }

    private fun markerOptions(item: PlacesGeo) {
        var place = LatLng(item.latitude.toString().toDouble(), item.longitude.toString().toDouble())
        var radious = item.promien.toString().toDouble()
        var markerOptions = MarkerOptions()
        markerOptions.position(place)
        markerOptions.title(item.nazwaMiejsca.toString())
        markerOptions.snippet(item.opis.toString())
        var circleOptions = CircleOptions()
            .center(place)
            .radius(radious)
            .fillColor(Color.parseColor("#2205c0f7"))
            .strokeColor(Color.parseColor("#008cf5"))
            .strokeWidth(5f)
        mMap.addMarker(markerOptions)
        mMap.addCircle(circleOptions)




    }

    private fun geofence(item: PlacesGeo) {

        geoClient = LocationServices.getGeofencingClient(this)
        var place = LatLng(item.latitude.toString().toDouble(), item.longitude.toString().toDouble())
        var radious = item.promien.toString().toDouble()
        //Geofence
        var geo = Geofence.Builder().setRequestId("Geo${item.id}")
            .setCircularRegion(
                place.latitude,
                place.longitude,
                radious.toFloat()
            )
            .setExpirationDuration(60*60*1000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                    or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        geoClient.addGeofences(getGeofencingRequest(geo),getGeofencePendingIntent())
            .addOnSuccessListener {
                println("Geofence dodany." )
            }
            .addOnFailureListener {
                println("Geofence nie zosał dodany.")
            }
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }
    private fun getGeofencePendingIntent(): PendingIntent {
        var intent = Intent("mini_projekt_1_jakub_wegrzyn_2_ACTION_SEND",null,this,GeofenceReceiver::class.java)
        return PendingIntent.getBroadcast(
            this, 0,
            intent,
            PendingIntent.FLAG_MUTABLE)
    }


}