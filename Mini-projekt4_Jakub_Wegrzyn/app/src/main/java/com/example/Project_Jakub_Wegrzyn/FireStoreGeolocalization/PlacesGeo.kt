package com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization

data class PlacesGeo(
    var id: String? = null,
    var nazwaMiejsca: String? = null,
    val opis: String? = null,
    val promien: Int? = null,
    val latitude: String? = null,
    val longitude: String? = null
)