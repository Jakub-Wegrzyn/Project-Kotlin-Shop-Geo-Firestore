package com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct

import com.google.firebase.firestore.DocumentId


data class Product
    (
    var uid: String ?= null,
    var nazwa: String ?= null,
    var cena: Double ?= null,
    var ilosc: Int ?= null,
    @DocumentId
    var documentId: String ? = null
    )