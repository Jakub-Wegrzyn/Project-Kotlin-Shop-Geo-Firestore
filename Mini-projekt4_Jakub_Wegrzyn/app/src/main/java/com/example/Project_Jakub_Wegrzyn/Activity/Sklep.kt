package com.example.Project_Jakub_Wegrzyn.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.*
import com.example.Project_Jakub_Wegrzyn.FireStoreDataUser.UserViewModel
import com.example.Project_Jakub_Wegrzyn.R
import com.example.Project_Jakub_Wegrzyn.StartApp.StartApp
import com.example.Project_Jakub_Wegrzyn.databinding.SklepBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Sklep : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserViewModel
    private val repository = ProductRepository()
    var fabVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding3 = SklepBinding.inflate(layoutInflater)
        setContentView(binding3.root)


        binding3.rv1.layoutManager = LinearLayoutManager(this)

        binding3.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        productViewModel = ProductViewModel(application)
        userViewModel = UserViewModel(application)


        productViewModel.products.observe(this, Observer { products ->
            products?.let {
                (binding3.rv1.adapter as ProductAdapter).setProducts(it)
            }
        })

        binding3.rv1.adapter = ProductAdapter(productViewModel)



        fabVisible = false

        val btn_list = binding3.btnList
        val btn_dodaj_do_koszyka = binding3.btnDodajDoKoszyka
        val btn_modyfikuj = binding3.btnModyfikuj
        val btn_usun = binding3.btnUsun

        btn_list.setOnClickListener {
            if (!fabVisible) {

                btn_dodaj_do_koszyka.show()
                btn_modyfikuj.show()
                btn_usun.show()

                btn_dodaj_do_koszyka.visibility = View.VISIBLE
                btn_usun.visibility = View.VISIBLE
                btn_modyfikuj.visibility = View.VISIBLE

                btn_list.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))

                fabVisible = true
            } else {

                btn_dodaj_do_koszyka.hide()
                btn_modyfikuj.hide()
                btn_usun.hide()

                btn_dodaj_do_koszyka.visibility = View.GONE
                btn_modyfikuj.visibility = View.GONE
                btn_usun.visibility = View.GONE

                btn_list.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))

                fabVisible = false
            }
        }

        btn_dodaj_do_koszyka.setOnClickListener {
            val array =
                (binding3.rv1.adapter as ProductAdapter).getCheckedArray() // lista zaznaczonych produktów
            closeKeyBoard()


            var products = repository.getProducts()
            var productsbasic = repository.getProducts()
            var productsapprove = ArrayList<Product>()
            var tester = true


            for (singleproduct in array) {
                products.observe(this, Observer { products ->
                    products.let {
                        products.forEach { product ->
                            if (singleproduct.uid == product.uid) {
                                var wynik = singleproduct.ilosc?.compareTo(0)
                                if (wynik == 1) {
                                    product.ilosc = product.ilosc!! - singleproduct.ilosc!!
                                    productsapprove.add(singleproduct)
                                    if (product.ilosc!! < 0) {
                                        tester = false
                                    }
                                } else {

                                    tester = false
                                }
                            }
                        }
                    }
                })
            }


            runOnUiThread {
                Handler().postDelayed({
                    if (!productsapprove.isEmpty()) {
                        if (tester == true) {

                            productsbasic.observe(this, Observer { products ->
                                products.let {
                                    (binding3.rv1.adapter as ProductAdapter).setProducts(
                                        products
                                    )
                                }
                            })

                            var map = HashMap<String, Int>()
                            var drugamapa = HashMap<String, Int>()
                            map.clear()

                            //Dodawanie do koszyka

                            var userData = userViewModel.getUserShoppingListMap()


                            runOnUiThread {
                                Handler().postDelayed({
                                    userData.observe(this, Observer { user ->
                                        user.forEach { x ->
                                            map.put(
                                                x.key,
                                                x.value
                                            ) //dodane wczesniej do koszyka produkty
                                        }
                                    })
                                }, 200L)
                            }

                            runOnUiThread {
                                Handler().postDelayed({
                                    if (map.isEmpty()) {
                                        runOnUiThread {
                                            Handler().postDelayed({
                                                productsapprove.forEach { pobranyzui ->
                                                    drugamapa.put(
                                                        pobranyzui.uid.toString(),
                                                        pobranyzui.ilosc!!
                                                    )
                                                }
                                            }, 400L)
                                        }
                                    } else {
                                        runOnUiThread {
                                            productsapprove.forEach { pobranyzui ->
                                                Handler().postDelayed({
                                                    map.forEach { pobranyzbazy ->
                                                        if (pobranyzbazy.key.equals(pobranyzui.uid)) {
                                                            var x =
                                                                pobranyzbazy.value + pobranyzui.ilosc!!
                                                            runOnUiThread {
                                                                Handler().postDelayed({
                                                                    productsbasic.observe(
                                                                        this,
                                                                        Observer { item ->
                                                                            item.let {
                                                                                item.forEach { item ->
                                                                                    if (item.uid.equals(
                                                                                            pobranyzbazy.key
                                                                                        )
                                                                                    ) {
                                                                                        if (x > item.ilosc!!) {
                                                                                            drugamapa.put(
                                                                                                pobranyzbazy.key,
                                                                                                item.ilosc!!
                                                                                            )
                                                                                        } else {
                                                                                            drugamapa.put(
                                                                                                pobranyzbazy.key,
                                                                                                x
                                                                                            )
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        })
                                                                }, 300L)
                                                            }

                                                        } else {
                                                            drugamapa.putAll(map)
                                                            drugamapa.put(
                                                                pobranyzui.uid!!,
                                                                pobranyzui.ilosc!!
                                                            )
                                                        }

                                                    }

                                                }, 400L)
                                            }
                                        }
                                    }
                                }, 500L)
                            }


                            GlobalScope.launch {
                                delay(1600L)
                                userViewModel.add_to_cart(drugamapa)
                                map.clear()
                            }

                            runOnUiThread {
                                Handler().postDelayed({
                                    Toast.makeText(
                                        binding3.rv1.context,
                                        "Dodano do koszyka",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }, 1600L)
                            }


                        } else {
                            var oldproducts = repository.getProducts()
                            oldproducts.observe(this, Observer { oldproducts ->
                                oldproducts.forEach {
                                    (binding3.rv1.adapter as ProductAdapter).setProducts(oldproducts)
                                }
                            })
                            Toast.makeText(
                                binding3.rv1.context,
                                "Wprowadzono nieprawidłowe dane, spróbuj jeszcze raz",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {
                        var oldproducts = repository.getProducts()
                        oldproducts.observe(this, Observer { oldproducts ->
                            oldproducts.forEach {
                                (binding3.rv1.adapter as ProductAdapter).setProducts(oldproducts)
                            }
                        })
                        Toast.makeText(
                            binding3.rv1.context,
                            "Wprowadzono nieprawidłowe dane, spróbuj jeszcze raz",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }, 1000L)
            }
        }

        btn_usun.setOnClickListener() {
            val array = (binding3.rv1.adapter as ProductAdapter).getCheckedArray()
            (binding3.rv1.adapter as ProductAdapter).delete(array)
            closeKeyBoard()
        }

        btn_modyfikuj.setOnClickListener {
            val arraycheckedlist = (binding3.rv1.adapter as ProductAdapter).getCheckedArray()
            if (arraycheckedlist.size == 1) {
                val intent6 = Intent(this, Modyfikacja::class.java).apply {
                    putExtra("id", arraycheckedlist.get(0).uid.toString())
                    putExtra("Nazwa", arraycheckedlist.get(0).nazwa)
                    putExtra("Cena", arraycheckedlist.get(0).cena)
                    putExtra("Ilosc", arraycheckedlist.get(0).ilosc)
                }
                startActivity(intent6)
            } else if (arraycheckedlist.size > 1) {
                Toast.makeText(this, "Zaznaczono więcej niż jeden produkt", Toast.LENGTH_SHORT)
                    .show()
            } else if (arraycheckedlist.size == 0) {
                Toast.makeText(this, "Nic nie zaznaczono", Toast.LENGTH_SHORT).show()
            }
            closeKeyBoard()
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
