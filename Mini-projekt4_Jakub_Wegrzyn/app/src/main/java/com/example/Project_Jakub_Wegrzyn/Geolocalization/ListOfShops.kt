package com.example.Project_Jakub_Wegrzyn.Geolocalization

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.PlacesGeo
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.UserGeoAdapter
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.UserGeoViewModel
import com.example.Project_Jakub_Wegrzyn.databinding.ActivityListOfShopsBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*


class ListOfShops : AppCompatActivity() {

    private lateinit var binding6 : ActivityListOfShopsBinding
    private lateinit var  geocoder: Geocoder
    private lateinit var userGeoViewModel : UserGeoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding6 = ActivityListOfShopsBinding.inflate(layoutInflater)
        setContentView(binding6.root)

        userGeoViewModel = UserGeoViewModel(application)

        binding6.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding6.rv1.layoutManager = LinearLayoutManager(this)

        userGeoViewModel.localizations.observe(this, Observer {
            userGeo ->
            userGeo?.let {
                (binding6.rv1.adapter as UserGeoAdapter).setUserGeoPlaces(it)

            }
        })

        binding6.rv1.adapter = UserGeoAdapter(userGeoViewModel)

        binding6.btnAkceptuj.setOnClickListener {
            insertDataToDatabaseUserGeo()
             binding6.edtxNazwaMiejsca.setText("")
             binding6.edtxOpis.setText("")
             binding6.edtxPromien.setText("")
             Toast.makeText(this@ListOfShops, "Pomyślnie dodano do bazy danych", Toast.LENGTH_SHORT).show()

            userGeoViewModel = UserGeoViewModel(application)
            runOnUiThread{
                Handler().postDelayed({
                    userGeoViewModel.localizations.observe(this, Observer {
                            userGeo ->
                        userGeo?.let {
                            (binding6.rv1.adapter as UserGeoAdapter).setUserGeoPlaces(it)

                        }
                    })
                },1000L)
            }

        }
    }

    override fun onResume() {
        super.onResume()

        geocoder = Geocoder(this, Locale.getDefault())
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
            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as
                LocationManager
        val criteria = Criteria()
        criteria.isAltitudeRequired = true
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.NO_REQUIREMENT
        val provider = locationManager.getBestProvider(criteria, false)


        var loc = Location(provider)
        val ll = object: LocationListener {
            override fun onLocationChanged(location: Location) {
                var address = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                try{
                    binding6.txtLongitude.text = address[0].longitude.toString()
                    binding6.txtLatitude.text = address[0].latitude.toString()
                }
                catch (exc: IndexOutOfBoundsException ){

                }
                loc = location
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
                Toast.makeText(this@ListOfShops, "Dostawca włączony!",
                    Toast.LENGTH_SHORT).show()
            }
            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
                Toast.makeText(this@ListOfShops, "Dostawca wyłączony!",
                    Toast.LENGTH_SHORT).show()
            }
        }
        locationManager.requestLocationUpdates(provider.toString(), 1000L, 1F, ll)
        //GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            1000L, 1F, ll)

    }
    fun insertDataToDatabaseUserGeo(){
        val nazwaMiejsca = binding6.edtxNazwaMiejsca.text.toString()
        val opisMiejsca = binding6.edtxOpis.text.toString()
        val promien = binding6.edtxPromien.text.toString().toInt()
        val longitude = binding6.txtLongitude.text.toString()
        val latitude = binding6.txtLatitude.text.toString()

        val data = PlacesGeo(null, nazwaMiejsca, opisMiejsca, promien, latitude, longitude)
        userGeoViewModel.addArray(data)

    }
}