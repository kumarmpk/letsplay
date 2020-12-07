package com.example.letsplay.fragments

import CreateUserObject
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.models.LoggedInUserDtls
import com.example.letsplay.common.Validation
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore


class WelcomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
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

                val email : String = view.findViewById<TextInputLayout>(R.id.emailAddress).editText?.text.toString()
                LoggedInUserDtls.emailAddress = email

                FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
                    .document(email).get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            document?.data?.forEach { item ->
                                if(item.key.equals("emailAddress") && item.value.equals(email)){
                                    validator.createSuccessToast("Email Address is already registered. Kindly login to use the app.", requireContext())
                                    var createObj = CreateUserObject()
                                    LoggedInUserDtls.loggedInUser = createObj.createUserObjectFromDocumentSnapShot(document)
                                    LoggedInUserDtls.loggedInUser!!.newUser = false
                                    findNavController().navigate(R.id.action_welcomeFragment_to_signIn)
                                }
                            }

                        } else {
                            validator.createSuccessToast("New user. Kindly register to use the app.", requireContext())
                            findNavController().navigate(R.id.action_welcomeFragment_to_signUp)
                        }
                    }
                    .addOnFailureListener { exception ->
                        validator.createErrorToast("Application faced unexpected exception. Please contact support team.", requireContext())
                    }
            }
        }
    }

}