package com.example.Project_Jakub_Wegrzyn.StartApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.Project_Jakub_Wegrzyn.Activity.MainActivity
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.databinding.StartappBinding
import com.google.firebase.auth.FirebaseAuth

class StartApp : AppCompatActivity() {
    private val fbAuth = FirebaseAuth.getInstance()
    private val currentUser = fbAuth.currentUser
    private lateinit var binding : StartappBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

    }

    override fun onStart() {
        super.onStart()
        isCurrentUser()
    }

    private fun isCurrentUser() {
        if(currentUser?.getIdToken(true) != null){
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) //
            }
            startActivity(intent)
        }
    }

}