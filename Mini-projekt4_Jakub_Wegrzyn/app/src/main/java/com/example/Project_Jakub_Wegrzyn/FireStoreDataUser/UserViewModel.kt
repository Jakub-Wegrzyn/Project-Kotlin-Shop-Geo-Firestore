package com.example.Project_Jakub_Wegrzyn.FireStoreDataUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.switchMap
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.Product
import com.google.firebase.auth.FirebaseAuth

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var reposityory = UserReposityory()
    private val auth = FirebaseAuth.getInstance()

    fun createAccount(user: User) = reposityory.createAccount(user)

    fun updatePersonalData(user: User) = reposityory.updatePersonalData(user)

    fun getUserShoppingListMap() = reposityory.getUserShoppingListMap()

    fun add_to_cart(product: HashMap<String, Int>) = reposityory.add_to_cart(product)

    fun delete(list: List<Product>) = reposityory.delete(list)

    var userData = reposityory.getUserData()


    var zakupy = userData.switchMap  {
            reposityory.getListOfBoughtProduct(it.user_shopping_list) }



    }
