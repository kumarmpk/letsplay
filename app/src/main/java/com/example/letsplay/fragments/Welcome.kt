package com.example.letsplay.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.validation.Validation
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*


class WelcomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.sign_in_sign_up).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.emailAddress)

            val validator = Validation()
            val context = requireContext()
            if(validator.inputValidation(view, context, list)
                && validator.isEmailValid(R.id.emailAddress, view, context)) {

                /*val email : String = view.findViewById<TextInputLayout>(R.id.emailAddress).editText?.text.toString()
                val emailMap = Bundle()
                emailMap.putString("emailAddress", email)
                val nextFragment = SignUp()
                nextFragment.setArguments(emailMap)

                fragmentManager?.beginTransaction()?.add(R.id.container, fragment)?.commit();*/

                findNavController().navigate(R.id.action_welcomeFragment_to_signUp)
            }
        }
    }

}