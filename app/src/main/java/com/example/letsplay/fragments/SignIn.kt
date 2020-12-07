package com.example.letsplay.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.common.Validation
import com.example.letsplay.models.LetsPlayUser
import com.example.letsplay.models.LoggedInUserDtls
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

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

        val emailTextInputEditText = view.findViewById<TextInputEditText>(R.id.emailAddressTextInputEditText)
        emailTextInputEditText?.setText(LoggedInUserDtls.emailAddress)

        view.findViewById<Button>(R.id.forgot_password).setOnClickListener{
            findNavController().navigate(R.id.action_signIn_to_forgotPassword)
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_signIn_to_welcomeFragment)
        }
        view.findViewById<Button>(R.id.sign_in).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.emailAddressTextInputLayout)
            list.add(R.id.password)

            val validation = Validation()
            if(validation.inputValidation(view, requireContext(), list)) {

                val password = view.findViewById<TextInputLayout>(R.id.password).editText?.text.toString()

                LoggedInUserDtls.emailAddress?.let { it1 ->
                    FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
                        .document(it1).get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                document?.data?.forEach { item ->
                                    if(item.key.equals("password") && item.value.equals(password)){
                                        validation.createSuccessToast("Logged In successfully.", requireContext())
                                        findNavController().navigate(R.id.action_signIn_to_homeScreen)
                                    }
                                }
                            } else {
                                validation.createErrorToast("Email address is new. Please register and try.", requireContext())
                                findNavController().navigate(R.id.action_signIn_to_signUp)
                            }
                        }
                        .addOnFailureListener { exception ->
                            validation.createErrorToast("Application faced unexpected exception. Please contact support team.", requireContext())
                        }
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signIn_to_welcomeFragment)
            }
        })

    }

}