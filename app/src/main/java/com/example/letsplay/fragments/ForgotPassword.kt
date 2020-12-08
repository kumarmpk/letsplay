package com.example.letsplay.fragments

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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPassword : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        val securityQuestions = resources.getStringArray(R.array.securityQuestions)
        val securityQuestionsSpinner = view.findViewById<Spinner>(R.id.securityQuestion)
        if(securityQuestionsSpinner != null){
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, securityQuestions)
            securityQuestionsSpinner.adapter = adapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val emailTextInputEditText = view.findViewById<TextInputEditText>(R.id.emailAddressTextInputEditText)
        emailTextInputEditText?.setText(SessionDtls.loggedInUser?.emailAddress)

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.reset).setOnClickListener{

            val list = ArrayList<Int>()
            list.add(R.id.emailAddressTextLayout)
            list.add(R.id.userNameTextLayout)

            val spinnerList = ArrayList<Int>()
            spinnerList.add(R.id.sport1)

            val list2 = ArrayList<Int>()
            list2.add(R.id.securityAnswer)

            val list3 = ArrayList<Int>()
            list3.add(R.id.password)

            val validation = Validation()

            if(validation.inputValidation(view, requireContext(), list)
                && validation.inputValidationSpinner(view, requireContext(), spinnerList)
                && validation.inputValidation(view, requireContext(), list2)
                && validation.inputValidation(view, requireContext(), list3)) {

                val emailTextInputEditText = view.findViewById<TextInputEditText>(R.id.emailAddressTextInputEditText).text.toString()
                var user = SessionDtls.loggedInUser

                if(user?.emailAddress?.equals(emailTextInputEditText)!!){

                    val securityQuestion = view.findViewById<Spinner>(R.id.securityQuestion)
                    val securityQuestionStr = securityQuestion.getItemAtPosition(securityQuestion.selectedItemPosition).toString()

                    if(securityQuestionStr.equals(user!!.securityQuestion)){

                        val securityAnswer = view.findViewById<TextInputLayout>(R.id.securityAnswer).editText?.text.toString()

                        if(securityAnswer.equals(user!!.securityAnswer)){

                            val password = view.findViewById<TextInputLayout>(R.id.password).editText?.text.toString()
                            user.password = password

                            if (user != null) {
                                FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
                                    .document(emailTextInputEditText).set(
                                        user
                                    ).addOnSuccessListener {
                                        validation.createSuccessToast("Password reset Successfully!!!", requireContext())
                                        findNavController().navigate(R.id.action_forgotPassword_to_signIn)
                                    }.addOnFailureListener {
                                        validation.createErrorToast("Password reset failed. Kindly contact support team.", requireContext())
                                    }
                            }
                        } else{
                            validation.createErrorToast("Security answer does not match with our records." +
                                    "\n Kindly try again.", requireContext())
                        }

                    } else{
                        validation.createErrorToast("Security question does not match with our records." +
                                "\n Kindly try again.", requireContext())
                    }

                } else{
                    validation.createErrorToast("Email Id does not match with the database." +
                            " \nPlease restart the app or register to use the app.", requireContext())
                }
            }
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_forgotPassword_to_signIn)
        }



    }

}