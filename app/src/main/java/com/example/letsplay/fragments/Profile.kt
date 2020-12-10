package com.example.letsplay.fragments

import CreateUserObject
import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.common.Validation
import com.example.letsplay.models.SessionDtls
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_create.create_title
import kotlinx.android.synthetic.main.fragment_profile.*

class Profile : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_screen_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

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

        val user = SessionDtls.loggedInUser
        view.findViewById<TextInputLayout>(R.id.emailAddressTextLayout).editText?.setText(user?.emailAddress)
        view.findViewById<TextInputLayout>(R.id.password).editText?.setText(user?.password)
        view.findViewById<TextInputLayout>(R.id.userNameTextLayout).editText?.setText(user?.userName)
        view.findViewById<TextInputLayout>(R.id.securityAnswer).editText?.setText(user?.securityAnswer)

        var securityQuestionList = ArrayList<String>()
        securityQuestionList.add("")
        securityQuestionList.add("What is your birthplace?")
        securityQuestionList.add("When did you graduate high school?")
        securityQuestionList.add("What is primary school name?")

        var securityQuesIndex = 0
        for((index, value) in securityQuestionList.withIndex()){
            if(value.equals(user?.securityQuestion)){
                securityQuesIndex = index
            }
        }

        view.findViewById<Spinner>(R.id.securityQuestion).setSelection(securityQuesIndex)

        var sportList = ArrayList<String>()
        sportList.add("")
        sportList.add("Soccer")
        sportList.add("Cricket")
        sportList.add("Baseball")
        sportList.add("Badminton")
        sportList.add("Sking")
        sportList.add("Skating")

        var sport1Index : Int = 0
        for((index, value) in sportList.withIndex()){
            if(value.equals(user?.sport1)){
                sport1Index = index
            }
        }

        view.findViewById<Spinner>(R.id.sport1).setSelection(sport1Index)

        var sport2Index : Int = 0
        for((index, value) in sportList.withIndex()){
            if(value.equals(user?.sport2)){
                sport2Index = index
            }
        }

        view.findViewById<Spinner>(R.id.sport2).setSelection(sport2Index)

        var sport3Index : Int = 0
        for((index, value) in sportList.withIndex()){
            if(value.equals(user?.sport3)){
                sport3Index = index
            }
        }

        view.findViewById<Spinner>(R.id.sport3).setSelection(sport3Index)

        view.findViewById<Button>(R.id.update).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.userNameTextLayout)
            list.add(R.id.emailAddressTextLayout)
            list.add(R.id.password)

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
                val userObj = createUser.createUserObjectFromView(view)
                val emailStr = userObj.emailAddress

                if (emailStr != null) {
                    FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)
                        .document(emailStr).set(
                            userObj
                        ).addOnSuccessListener {
                            validator.createSuccessToast("Updated Successfully.", requireContext())
                            findNavController().navigate(R.id.action_profile_to_homeScreen)
                        }.addOnFailureListener {
                            validator.createErrorToast("Update failed. Kindly contact support team.", requireContext())
                            findNavController().navigate(R.id.action_profile_to_homeScreen)
                        }
                } else{
                    validator.createErrorToast("Something went wrong. Kindly restart the app.", requireContext())
                }
            }
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_profile_to_homeScreen)
        }

        profile_title.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.home_menu -> {
                    findNavController().navigate(R.id.action_profile_to_homeScreen)
                    true
                }
                R.id.create_menu -> {
                    findNavController().navigate(R.id.action_profile_to_create)
                    true
                }
                R.id.view_join_menu -> {
                    findNavController().navigate(R.id.action_profile_to_showAllEvents)
                    true
                }
                R.id.update_profile_menu -> {
                    findNavController().navigate(R.id.action_profile_self)
                    true
                }
                R.id.log_out_menu -> {
                    SessionDtls.loggedInUser = null
                    findNavController().navigate(R.id.action_profile_to_signIn)
                    true
                }
                else -> false
            }
        }

    }

}