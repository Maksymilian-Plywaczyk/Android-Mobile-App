package com.example.android_mobile_app

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android_mobile_app.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        binding.newUserText.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginButton.setOnClickListener {
            validateData()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
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
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_openFragment)
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