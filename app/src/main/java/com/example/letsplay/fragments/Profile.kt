package com.example.letsplay.fragments

import android.os.Bundle
import android.view.*
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Validation

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.update).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.emailAddress)
            list.add(R.id.password)
            list.add(R.id.sport1)

            val validator = Validation()
            if(validator.inputValidation(view, requireContext(), list)){
                validator.createSuccessToast("User Profile Updated Successfully!!!", requireContext())
                findNavController().navigate(R.id.action_profile_to_homeScreen)
            }
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_profile_to_homeScreen)
        }
    }

}