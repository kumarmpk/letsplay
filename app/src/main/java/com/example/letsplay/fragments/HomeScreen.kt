package com.example.letsplay.fragments

import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.models.LoggedInUserDtls
import kotlinx.android.synthetic.main.fragment_home_screen.*

class HomeScreen : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        System.out.println("a")
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
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.create).setOnClickListener{
            findNavController().navigate(R.id.action_homeScreen_to_create)
        }
        view.findViewById<Button>(R.id.view_join).setOnClickListener{
            findNavController().navigate(R.id.action_homeScreen_to_showAllEvents)
        }
        view.findViewById<Button>(R.id.update_profile).setOnClickListener{
            findNavController().navigate(R.id.action_homeScreen_to_profile)
        }
        home_screen_title.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.create_menu -> {
                    findNavController().navigate(R.id.action_homeScreen_to_create)
                    true
                }
                R.id.view_join_menu -> {
                    findNavController().navigate(R.id.action_homeScreen_to_showAllEvents)
                    true
                }
                R.id.update_profile_menu -> {
                    findNavController().navigate(R.id.action_homeScreen_to_profile)
                    true
                }
                R.id.log_out_menu -> {
                    LoggedInUserDtls.userName = ""
                    LoggedInUserDtls.emailAddress = ""
                    LoggedInUserDtls.isLoggedIn = false
                    findNavController().navigate(R.id.action_homeScreen_to_signIn)
                    true
                }
                else -> false
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_homeScreen_to_signIn)
            }
        })


    }


}