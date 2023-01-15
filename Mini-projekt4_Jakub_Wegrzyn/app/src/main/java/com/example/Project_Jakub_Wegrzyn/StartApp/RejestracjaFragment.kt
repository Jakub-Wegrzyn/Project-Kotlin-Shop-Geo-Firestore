package com.example.Project_Jakub_Wegrzyn.StartApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.example.Project_Jakub_Wegrzyn.FireStoreDataUser.User
import com.example.Project_Jakub_Wegrzyn.FireStoreDataUser.UserViewModel
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.UserGeo
import com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization.UserGeoViewModel
import com.example.Project_Jakub_Wegrzyn.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RejestracjaFragment : BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private val userViewModel by viewModels<UserViewModel>()
    private val userGeoViewModel by viewModels<UserGeoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.zarejestruj_fr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSignUpClick(view)
    }

    private fun setupSignUpClick(view: View) {
        val btn_zarejestruj = view.findViewById<Button>(R.id.btn_zaloguj)
        btn_zarejestruj.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.edtx_email_rej).text.toString()
            val haslo1 = view.findViewById<EditText>(R.id.edtx_password_rej).text.toString()
            val haslo2 = view.findViewById<EditText>(R.id.edtx_password2_rej).text.toString()

            if (haslo1 == haslo2) {
                fbAuth.createUserWithEmailAndPassword(email, haslo1)
                    .addOnSuccessListener { authRes ->
                        if (authRes.user != null) {
                            val data1 = User(authRes.user!!.uid, null, null, email, null)
                            userViewModel.createAccount(data1)
                            val data2 = UserGeo(null )
                            userGeoViewModel.createUserGeo(data2)
                            GlobalScope.launch {
                                delay(3000L)
                                userGeoViewModel.deleteNullValue()
                            }
                            GlobalScope.launch {
                                delay(4000L)
                                startApp()
                                Snackbar.make(
                                    requireView(),
                                    "Pomyślnie utworzono konto",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), "Coś poszło nie tak", Snackbar.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }


}