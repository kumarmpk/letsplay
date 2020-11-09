package com.example.letsplay.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.validation.Validation

class SignIn : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.forgot_password).setOnClickListener{
            findNavController().navigate(R.id.action_signIn_to_forgotPassword)
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_signIn_to_welcomeFragment)
        }
        view.findViewById<Button>(R.id.sign_in).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.emailAddress)
            list.add(R.id.password)

            val validation = Validation()
            if(validation.inputValidation(view, requireContext(), list)) {
                findNavController().navigate(R.id.action_signIn_to_homeScreen)
            }
        }
    }

}