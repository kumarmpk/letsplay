package com.example.letsplay.fragments

import CreateUserObject
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.common.Validation
import com.example.letsplay.models.SessionDtls
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        val securityQuestions = resources.getStringArray(R.array.securityQuestions)
        val securityQuestionsSpinner = view.findViewById<Spinner>(R.id.securityQuestion)
        if(securityQuestionsSpinner != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, securityQuestions)
            securityQuestionsSpinner.adapter = adapter
        }

        val sports1 = resources.getStringArray(R.array.sports1)
        val sportsSpinner1 = view.findViewById<Spinner>(R.id.sport1)
        if(sportsSpinner1 != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports1)
            sportsSpinner1.adapter = adapter
        }

        val sports = resources.getStringArray(R.array.sports)
        val sportsSpinner2 = view.findViewById<Spinner>(R.id.sport2)
        if(sportsSpinner2 != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports)
            sportsSpinner2.adapter = adapter
        }

        val sportsSpinner3 = view.findViewById<Spinner>(R.id.sport3)
        if(sportsSpinner3 != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports)
            sportsSpinner3.adapter = adapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var emailTextInputEditText = view.findViewById<TextInputEditText>(R.id.emailAddressEditText)
        emailTextInputEditText?.setText(SessionDtls.loggedInUser?.emailAddress)

        view.findViewById<Button>(R.id.sign_up).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.emailAddressTextLayout)
            list.add(R.id.password)
            list.add(R.id.userNameTextLayout)

            val spinnerList = ArrayList<Int>()
            spinnerList.add(R.id.sport1)
            spinnerList.add(R.id.securityQuestion)

            val list2 = ArrayList<Int>()
            list2.add(R.id.securityAnswer)

            val validator = Validation()
            if(validator.inputValidation(view, requireContext(), list)
                && validator.inputValidationSpinner(view, requireContext(), spinnerList)
                && validator.inputValidation(view, requireContext(), list2)){

                val createUser = CreateUserObject()
                val user = createUser.createUserObjectFromView(view)
                val emailStr = user.emailAddress

                if (emailStr != null) {
                    FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
                        .document(emailStr).set(
                            user
                        ).addOnSuccessListener {
                            validator.createSuccessToast("Registered Successfully!!! Welcome to home screen...", requireContext())
                            findNavController().navigate(R.id.action_signUp_to_homeScreen)
                        }.addOnFailureListener {
                            validator.createErrorToast("Registration failed. Kindly contact support team.", requireContext())
                            findNavController().navigate(R.id.action_signUp_to_welcomeFragment)
                        }
                }
            }
        }

        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_signUp_to_welcomeFragment)
        }
    }



}