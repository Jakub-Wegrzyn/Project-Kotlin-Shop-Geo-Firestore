package com.example.Project_Jakub_Wegrzyn.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.ProductRepository
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.ProductViewModel
import com.example.Project_Jakub_Wegrzyn.FireStoreDataUser.User
import com.example.Project_Jakub_Wegrzyn.FireStoreDataUser.UserAdapter
import com.example.Project_Jakub_Wegrzyn.FireStoreDataUser.UserViewModel
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.databinding.UserLayoutBinding
import com.google.firebase.auth.FirebaseAuth


class UserActivity : AppCompatActivity() {

    private lateinit var binding: UserLayoutBinding
    private val auth = FirebaseAuth.getInstance()
    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserViewModel
    private val repository = ProductRepository()

    var fabVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv1.layoutManager = LinearLayoutManager(this)

        binding.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        productViewModel = ProductViewModel(application)

        userViewModel = UserViewModel(application)

        userViewModel.zakupy.observe(this) { list ->
            list?.let {
                runOnUiThread {
                    Handler().postDelayed({
                        (binding.rv1.adapter as UserAdapter).setProducts(it)
                    }, 1000L)
                }
            }
        }

        binding.rv1.adapter = UserAdapter(this, userViewModel)

        var userdata = userViewModel.userData

        userdata.observe(this, Observer { user ->
            binding.edtxEmail.text = user.email
            binding.edtxImie.setText(user.name)
            binding.edtxNazwisko.setText(user.surname)
        })

        binding.btnAkceptuj.setOnClickListener {
            val name = binding.edtxImie.text.toString()
            val surname = binding.edtxNazwisko.text.toString()
            val user = User(auth.currentUser?.uid, name, surname, null, null)
            userViewModel.updatePersonalData(user)
            closeKeyBoard()
            Toast.makeText(this, "Pomyślnie zaktualizowano", Toast.LENGTH_SHORT).show()
        }


        fabVisible = false

        val btn_del = binding.btnDelete
        val btn_add = binding.btnAdd
        val btn_pay = binding.btnPay

        btn_add.setOnClickListener {
            if (!fabVisible) {

                btn_pay.show()
                btn_del.show()

                btn_pay.visibility = VISIBLE
                btn_del.visibility = VISIBLE

                btn_add.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))

                fabVisible = true
            } else {

                btn_pay.hide()
                btn_del.hide()

                btn_pay.visibility = GONE
                btn_del.visibility = GONE

                btn_add.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))

                fabVisible = false
            }
        }


        btn_pay.setOnClickListener {
            var cena_koszyka = (binding.rv1.adapter as UserAdapter).getCenaKoszyka()
            var produkty_kupione = (binding.rv1.adapter as UserAdapter).getCheckList()

            var products = repository.getProducts()
            var sprawdzam = true

            runOnUiThread {
                Handler().postDelayed({
                    products.observe(this, Observer { produkt ->
                        produkt.let{
                                produkt ->
                            produkt.forEach{
                                    item ->
                                produkty_kupione.forEach{
                                        pro_kup ->
                                    if(pro_kup.ilosc!! <0){
                                        sprawdzam = false
                                    }
                                    else{
                                        if(item.uid.equals(pro_kup.documentId)){
                                            item.ilosc = item.ilosc!! - pro_kup.ilosc!!.toInt()
                                            if(item.ilosc!!.toInt() <0)
                                            {
                                                sprawdzam = false
                                            }
                                            else{
                                                productViewModel.update(item)
                                                sprawdzam = true
                                            }
                                        }
                                    }


                                }
                            }
                        }
                    })
                },1000L)
            }

            runOnUiThread {
                Handler().postDelayed({
                    if(sprawdzam){
                        (binding.rv1.adapter as UserAdapter).delete(produkty_kupione)

                        AlertDialog.Builder(this)
                            .setTitle("Udany zakup")
                            .setMessage("Cena za zakupy: " + cena_koszyka.toString() + "zł")
                            .setPositiveButton("Okay"){
                                _,_ ->
                            }.show()
                        Toast.makeText(binding.rv1.context, "Zakupiono", Toast.LENGTH_SHORT).show()
                    }
                    else{

                        (binding.rv1.adapter as UserAdapter).refresh()
                        Toast.makeText(binding.rv1.context, "Nie zakupiono - Wprorwadziłeś błędną ilość", Toast.LENGTH_LONG).show()
                    }
                },1500L)
            }


        }

        btn_del.setOnClickListener {
            val list = (binding.rv1.adapter as UserAdapter).getCheckList()
            (binding.rv1.adapter as UserAdapter).delete(list)
            Toast.makeText(binding.rv1.context, "Usunięto z koszyka", Toast.LENGTH_LONG).show()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}