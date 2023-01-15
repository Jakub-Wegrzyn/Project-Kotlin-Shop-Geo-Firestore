package com.example.Project_Jakub_Wegrzyn.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.StartApp.StartApp
import com.example.Project_Jakub_Wegrzyn.databinding.OptionLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OptionsActivity :  AppCompatActivity() {

    lateinit var optionsManager: OptionsManager
    var pole_tekst = ""

    private lateinit var binding4 : OptionLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding4 = OptionLayoutBinding.inflate(layoutInflater)
        setContentView(binding4.root)

        optionsManager = OptionsManager(this)

        buttonSave()

        observeData()

        binding4.fb4.setOnClickListener(){
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }

    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun observeData() {
        optionsManager.wpisanyTekstFlow.asLiveData().observe(
            this,
        ) {
            pole_tekst = it
            if(binding4.poleTekstowe.text.isEmpty()){
                pole_tekst = ""
            }
            binding4.poleTekstowe.text = it.toString()
        }

        optionsManager.KolorFlow.asLiveData().observe(
            this,
        ) {
            if(it)
                binding4.cr.setBackgroundColor(Color.RED)
            else
                binding4.cr.setBackgroundColor(R.color.purple)

        }
    }

    private fun buttonSave() {

        binding4.btnSave.setOnClickListener {

            if(binding4.wpiszTekst.text.toString().equals("")){
                pole_tekst = ""
                Toast.makeText(this, "Nie wpisałeś tekstu", Toast.LENGTH_LONG).show()
            }
            else{
                pole_tekst = binding4.wpiszTekst.text.toString()
                Toast.makeText(this, "Wpisano: \n" + pole_tekst, Toast.LENGTH_LONG).show()
            }
            val isKolor = binding4.wybierzKolor.isChecked


            GlobalScope.launch {
                optionsManager.storeData(pole_tekst, isKolor)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_functions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.wyloguj){
            Toast.makeText(this, "Pomyślnie wylogowano", Toast.LENGTH_LONG).show()
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
