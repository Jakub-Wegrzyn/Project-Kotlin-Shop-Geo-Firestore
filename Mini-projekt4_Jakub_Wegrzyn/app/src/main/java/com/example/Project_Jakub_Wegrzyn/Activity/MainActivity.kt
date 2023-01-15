package com.example.Project_Jakub_Wegrzyn.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.Project_Jakub_Wegrzyn.Geolocalization.ListOfShops
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.StartApp.StartApp
import com.example.Project_Jakub_Wegrzyn.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.zarzadzanie.setOnClickListener{
            val intent1 = Intent(this, Zarzadzanie::class.java)
            startActivity(intent1)
        }


        binding.sklep.setOnClickListener{
            val intent2 = Intent(this, Sklep::class.java)
            startActivity(intent2)
        }

        binding.opcje.setOnClickListener{
            val intent3 = Intent(this, OptionsActivity::class.java)
            startActivity(intent3)
        }

        binding.mapa.setOnClickListener {
            val intent4 = Intent(this, com.example.Project_Jakub_Wegrzyn.Geolocalization.MapsActivity::class.java)
            startActivity(intent4)
        }

        binding.listaSklepow.setOnClickListener{
            val intent5 = Intent(this, ListOfShops::class.java)
            startActivity(intent5)
        }


        //autoryzacja
        var currentUser = auth.currentUser?.getIdToken(true)
        if(currentUser == null){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(applicationContext, StartApp::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_functions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.wyloguj){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(applicationContext, StartApp::class.java))
        }
        if(item.itemId == R.id.moj_profil){
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}