package com.example.android_mobile_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android_mobile_app.databinding.FragmentOpenBinding
import com.example.android_mobile_app.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class OpenFragment : Fragment() {
    private lateinit var binding: FragmentOpenBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding     = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open, container, false
        )
        binding.lougoutButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_openFragment_to_loginFragment)
        }
        mAuth = Firebase.auth
        return binding.root
    }
}