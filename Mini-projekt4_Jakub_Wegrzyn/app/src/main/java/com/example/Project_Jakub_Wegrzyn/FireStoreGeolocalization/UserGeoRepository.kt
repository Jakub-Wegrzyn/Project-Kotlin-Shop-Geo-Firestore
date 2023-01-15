package com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserGeoRepository {
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    fun createUserGeo(userGeo: UserGeo){
        val uid = auth.currentUser?.uid
        uid?.let {
            cloud.collection("usersGeo")
                .document(uid.toString())
                .set(userGeo)
        }
    }
    fun deleteNull(){
        val uid = auth.currentUser?.uid
        cloud.collection("usersGeo")
            .document(uid.toString())
            .update("uidOfPlacesGeo", FieldValue.delete())
            .addOnSuccessListener {
                println("Usunięto pomyślnie zbędne pole")
            }
            .addOnFailureListener {
                println("Nie usunięto pomyślnie zbędnego pola")
            }
    }

    fun addArrayField(data: PlacesGeo) {
        val uid = auth.currentUser?.uid
        var id = randomOfId()
        data.id = id

        cloud.collection("usersGeo")
            .document(uid.toString())
            .update("$id", data)
            .addOnSuccessListener {
                println("DODANO")
            }
            .addOnFailureListener {
                println("NIEDODANO")
            }
    }

    fun randomOfId(): String {
        val charPool : List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..30)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun getLocalizations(): LiveData<List<PlacesGeo>> {
        val uid = auth.currentUser?.uid
        val cloudResult = MutableLiveData<List<PlacesGeo>>()
        cloud.collection("usersGeo")
            .document("$uid").get()
            .addOnSuccessListener {
                result ->
                if(result.data?.contains("uidOfPlacesGeo") == true){
                    null
                }
                else{
                    var data = result.data
                    data as HashMap<String, HashMap<String, Any>>
                    var array = ArrayList<PlacesGeo>()

                    for(i in data){

                        var placesGeo = PlacesGeo(i.value.get("id").toString(),i.value.get("nazwaMiejsca").toString(), i.value.get("opis").toString(),
                            i.value.get("promien").toString().toInt(), i.value.get("latitude").toString(), i.value.get("longitude").toString() )
                        array.add(placesGeo)
                    }
                    cloudResult.postValue(array)
                }
            }
            .addOnFailureListener {
                println("Błąd")
            }

        return cloudResult
    }
}