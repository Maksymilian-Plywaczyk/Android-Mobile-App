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
import com.example.android_mobile_app.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register, container, false
        )
        mAuth = Firebase.auth
        binding.userText.setOnClickListener { view:View ->view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        binding.signUpButton.setOnClickListener {
            validateData()
        }
        return binding.root
    }

    private fun validateData() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()

        //validate Data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.loginEmail.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            binding.loginPassword.error = "Please enter password"
        } else if (password.length < 6) {
            binding.loginPassword.error = "Password must atleast 6 characters long"
        } else {
            firebaseSingUp()
        }
    }

    private fun firebaseSingUp() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val firebaseUser = mAuth.currentUser
            val email = firebaseUser!!.email
            Toast.makeText(activity, "SingUp as $email", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(activity, MainActivity::class.java))
//            activity?.finish()
        }.addOnFailureListener { e ->
            Toast.makeText(activity, "SingUp  failed due to ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

}