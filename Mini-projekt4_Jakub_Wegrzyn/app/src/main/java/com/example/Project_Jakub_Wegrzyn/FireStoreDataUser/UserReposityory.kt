package com.example.Project_Jakub_Wegrzyn.FireStoreDataUser

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserReposityory {
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    fun createAccount(user: User) {
        user.uid?.let {
            cloud.collection("users")
                .document(it)
                .set(user)
        }
    }

    fun getUserData(): LiveData<User> {
        var cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid

        cloud.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user!!)
            }
            .addOnFailureListener {
                Log.d("REPO_USER", it.message.toString())
            }

        return cloudResult
    }

    fun updatePersonalData(user: User) {
        user.uid?.let {
            cloud.collection("users")
                .document(it)
                .update(
                    "name", user.name,
                    "surname", user.surname
                )
                .addOnSuccessListener {
                    Log.d("REPO_USER", "Pomyślnie zaktualizowano")
                }
                .addOnFailureListener {
                    Log.d("REPO_USER", "Nie zaktualizowano")
                }
        }
    }

    fun add_to_cart(products: HashMap<String, Int>) {
        val uid = auth.currentUser?.uid


        cloud.collection("users")
            .document(uid!!)
            .update(
                "user_shopping_list", products
            )
            .addOnSuccessListener {
                Log.d("REPO_USER", "Pomyślnie zaktualizowano")
            }
            .addOnFailureListener {
                Log.d("REPO_USER", "Nie zaktualizowano")
            }
    }


    fun getListOfBoughtProduct(map: Map<String, Int>?): LiveData<List<Product>> {
        val cloudResult = MutableLiveData<List<Product>>()
        val cloudArrayList = ArrayList<Product>()


        if (!map.isNullOrEmpty()) {
            map.forEach { item ->
                cloud.collection("shop")
                    .document(item.key)
                    .get()
                    .addOnSuccessListener {
                        val resultProduct = it.toObject(Product::class.java)
                        if (resultProduct != null) {
                            resultProduct.ilosc = item.value
                            cloudArrayList.add(resultProduct)
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Get", "Nie znaleziono")
                    }
            }

        } else {
            Log.d(TAG, "No data")
        }
        cloudResult.postValue(cloudArrayList)

        return cloudResult

    }


    fun delete(list: List<Product>) {
        val uid = auth.currentUser?.uid

        if (!list.isNullOrEmpty()) {
            list.forEach { product ->
                product.uid = product.documentId
                product.documentId = null
                cloud.collection("users")
                    .document(uid!!)
                    .update(
                        "user_shopping_list." + product.uid, FieldValue.delete()
                    )
            }
        }
    }

    fun getUserShoppingListMap(): LiveData<Map<String, Int>> {
        var cloudResult = MutableLiveData<Map<String, Int>>()
        val uid = auth.currentUser?.uid

        cloud.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                var map = user?.user_shopping_list
                map?.forEach{
                    cloudResult.postValue(map!!)
                }

            }
            .addOnFailureListener {
                Log.d("REPO_USER", it.message.toString())
            }


        return cloudResult


    }
}

