package com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository {
    private val cloud = FirebaseFirestore.getInstance()

    fun getProducts(): LiveData<List<Product>>{

        var cloudResult = MutableLiveData<List<Product>>()

        cloud.collection("shop")
            .get()
            .addOnSuccessListener {
                println("ittttttttt " + it)
                val product = it.toObjects(Product::class.java)
                product.forEach{
                    x ->
                    x.uid = x.documentId
                    x.documentId = null
                }
                cloudResult.postValue(product)
            }
            .addOnFailureListener {
                Log.d("REPO", it.message.toString())
            }
        return cloudResult
    }

    fun update(product: Product){
        cloud.collection("shop")
            .document(product.uid.toString())
            .update(
                "nazwa", product.nazwa,
                "cena", product.cena,
                "ilosc", product.ilosc
            )
    }

    fun insert(product: Product) {
        cloud.collection("shop")
            .add(product)
            .addOnSuccessListener {
                Log.d("InsertIntoCloud", "Dodano do chmury" )
            }
            .addOnFailureListener{
                Log.d("InsertIntoCloud", "Nie dodano do chmury" )
            }
    }

    fun delete(product: Product) {
        cloud.collection("shop")
            .document(product.uid.toString())
            .delete()
    }
}