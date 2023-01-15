package com.example.Project_Jakub_Wegrzyn.FireStoreDataUser

data class User
    (
    val uid: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val user_shopping_list: Map<String, Int> ?= null
)