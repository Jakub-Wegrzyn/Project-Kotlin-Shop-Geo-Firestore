package com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductRepository()

    var products = repository.getProducts()

    fun update(product: Product) = repository.update(product)
    fun insert(product: Product) = repository.insert(product)
    fun delete(product: Product) = repository.delete(product)

}