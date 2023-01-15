package com.example.Project_Jakub_Wegrzyn.StartApp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.Project_Jakub_Wegrzyn.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRegistrationClick(view)
        setupLoginClick(view)

    }

    private fun setupRegistrationClick(view: View) {
        val btn_zarejestruj = view.findViewById<Button>(R.id.btn_zarejestruj)
        btn_zarejestruj.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_rejestracjaFragment22)
        }
    }

    private fun setupLoginClick(view: View) {
        val btn_zaloguj = view.findViewById<Button>(R.id.btn_zaloguj)
        btn_zaloguj.setOnClickListener {

            var email = view.findViewById<EditText>(R.id.edtx_email_rej).text.toString()
            var haslo = view.findViewById<EditText>(R.id.edtx_password_rej).text.toString()

            if (email != "" && haslo != "") {
                fbAuth.signInWithEmailAndPassword(email, haslo)
                    .addOnSuccessListener { authRes ->
                        if (authRes.user != null) startApp()

                    }.addOnFailureListener { exc ->
                        Snackbar.make(requireView(), "Coś poszło nie tak", Snackbar.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Snackbar.make(requireView(), "Nic nie wpisaleś...", Snackbar.LENGTH_LONG).show()
            }

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)

        }
    }


}