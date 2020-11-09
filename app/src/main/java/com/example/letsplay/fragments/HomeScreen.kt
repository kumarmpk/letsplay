package com.example.letsplay.fragments

import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R

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
            findNavController().navigate(R.id.action_homeScreen_to_view)
        }
    }


}