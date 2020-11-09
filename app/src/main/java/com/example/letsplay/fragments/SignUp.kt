package com.example.letsplay.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.validation.Validation

class SignUp : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*val email : String? = arguments?.getString("emailAddress")
        System.out.println("eeeeeeeeee: "+email)*/
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.sign_up).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.emailAddress)
            list.add(R.id.password)
            list.add(R.id.sport1)

            val validator = Validation()
            if(validator.inputValidation(view, requireContext(), list)){
                validator.createSuccessToast("Registered Successfully!!!", requireContext())
                findNavController().navigate(R.id.action_signUp_to_signIn)
            }
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_signUp_to_welcomeFragment)
        }
    }



}