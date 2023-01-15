package com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class UserGeoViewModel(application: Application) : AndroidViewModel(application) {
    private var repository = UserGeoRepository()

    var localizations = repository.getLocalizations()

    fun createUserGeo(userGeo: UserGeo) = repository.createUserGeo(userGeo)

    fun deleteNullValue() = repository.deleteNull()

    fun addArray(data: PlacesGeo) = repository.addArrayField(data)
}