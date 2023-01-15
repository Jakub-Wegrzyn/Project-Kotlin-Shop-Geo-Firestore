package com.example.Project_Jakub_Wegrzyn.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.Product
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.ProductViewModel
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.StartApp.StartApp
import com.example.Project_Jakub_Wegrzyn.databinding.ZarzadzanieBinding
import com.google.firebase.auth.FirebaseAuth

class Zarzadzanie : AppCompatActivity() {

    private lateinit var binding2: ZarzadzanieBinding
    private lateinit var mProductViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding2 = ZarzadzanieBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        binding2.dodajProdukt.setOnClickListener {
            insertDataToDatabase()
            closeKeyBoard()

            val intent = Intent("com.example.mini_projekt_1_jakub_wegrzyn.ACTION_SEND").apply {
                putExtra("NAZWA_PRODUKTU", binding2.addNazwaProd.text.toString())
                putExtra("CENA", binding2.addCena.text.toString())
                putExtra("ILOSC", binding2.addIlosc.text.toString())
            }
            sendBroadcast(intent)
            binding2.addCena.text = null
            binding2.addIlosc.text = null
            binding2.addNazwaProd.text = null
        }

        binding2.fb1.setOnClickListener() {
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }
    }

    private fun insertDataToDatabase() {

        val nazwa_prod = binding2.addNazwaProd.text.toString()
        val cena = binding2.addCena.text.toString()
        val ilosc = binding2.addIlosc.text.toString()

        if (inputCheck(nazwa_prod, cena, ilosc)) {
            if (checkNumber(cena, ilosc)) {
                val data = Product(null, nazwa_prod, cena.toDouble(), ilosc.toInt(), null)
                mProductViewModel.insert(data)
                Toast.makeText(this, "Dodano pomyślnie", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Podano błędne dane", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, "Brak danych", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkNumber(cena: String, ilosc: String): Boolean {
        return try {
            cena.toDouble()
            ilosc.toInt()
            true

        } catch (exc: NumberFormatException) {
            false
        }
    }

    private fun inputCheck(nazwaProd: String, cena: String, ilosc: String): Boolean {
        return !(nazwaProd.isEmpty() || cena.isEmpty() || ilosc.isEmpty())
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_functions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.wyloguj) {
            Toast.makeText(this, "Pomyślnie wylogowano", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(applicationContext, StartApp::class.java))
        }
        if (item.itemId == R.id.moj_profil) {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
