package com.example.android_mobile_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android_mobile_app.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.Main


class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private var email = ""
    private var password = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )
        mAuth = Firebase.auth
        checkUser()
        binding.newUserText.setOnClickListener {view:View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginButton.setOnClickListener {
            validateData()
        }
        return binding.root
    }

    private fun validateData() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()

        //validate Data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.loginEmail.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            binding.loginPassword.error = "Please enter password"
        } else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val firebaseUser = mAuth.currentUser
            val email = firebaseUser!!.email
            Toast.makeText(activity, "LoggedIn as $email", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(activity, MainActivity::class.java))
//            activity?.finish()
        }.addOnFailureListener { e ->
            Toast.makeText(activity, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUser() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
//            startActivity(Intent(activity, MainActivity::class.java))
//            activity?.finish()
        }
    }
}