package com.example.Project_Jakub_Wegrzyn.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.Product
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.ProductViewModel
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.StartApp.StartApp
import com.example.Project_Jakub_Wegrzyn.databinding.ModyfikacjaBinding
import com.google.firebase.auth.FirebaseAuth


class Modyfikacja: AppCompatActivity() {

    private lateinit var binding5 : ModyfikacjaBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding5 = ModyfikacjaBinding.inflate(layoutInflater)

        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        setContentView(binding5.root)

        val uid = intent.getStringExtra("id")

        val nazwa = intent.getStringExtra("Nazwa")
        val cena = intent.getDoubleExtra("Cena", 0.0)
        val ilosc = intent.getIntExtra("Ilosc", 0)


        binding5.nazwaProdMod.setText(nazwa)
        binding5.cenaMod.setText(cena.toString())
        binding5.iloscMod.setText(ilosc.toString())

        binding5.wroc.setOnClickListener {
            val intent1 = Intent(this, Sklep::class.java)
            startActivity(intent1)
        }

        binding5.button.setOnClickListener{
            val data = Product(uid,binding5.nazwaProdMod.text.toString(), binding5.cenaMod.text.toString().toDouble(), binding5.iloscMod.text.toString().toInt(), null)
            productViewModel.update(data)
            Toast.makeText(this, "Zaktualizowano", Toast.LENGTH_LONG).show()
        }

        binding5.usu.setOnClickListener{
            val data = Product(uid, binding5.nazwaProdMod.text.toString(), binding5.cenaMod.text.toString().toDouble(), binding5.iloscMod.text.toString().toInt(), null)
            productViewModel.delete(data)
            val intent1 = Intent(this, Sklep::class.java)
            startActivity(intent1)
            Toast.makeText(this, "Usunieto", Toast.LENGTH_LONG).show()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, Sklep::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_functions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.wyloguj){
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Pomyślnie wylogowano", Toast.LENGTH_LONG).show()
            startActivity(Intent(applicationContext, StartApp::class.java))
        }
        if(item.itemId == R.id.moj_profil){
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
