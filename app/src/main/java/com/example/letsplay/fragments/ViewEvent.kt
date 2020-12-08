package com.example.letsplay.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.common.Validation

class ViewEvent : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_view, container, false)

        /*var cusSupportManager = activity?.supportFragmentManager
        if (cusSupportManager != null) {
            var fragments = cusSupportManager?.fragments

            if(fragments != null && fragments.size > 1){
                val showAllId = cusSupportManager?.findFragmentById(R.id.ShowAllEventsConstraintLayout)
                if(showAllId != null){
                    cusSupportManager.beginTransaction().remove(showAllId).commit()
                }
            }
        }*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.interested).setOnClickListener{
            findNavController().navigate(R.id.action_view_to_homeScreen)
        }
        view.findViewById<Button>(R.id.declined).setOnClickListener{
            findNavController().navigate(R.id.action_view_to_homeScreen)
        }
        view.findViewById<Button>(R.id.update).setOnClickListener{
            val list = ArrayList<Int>()
            list.add(R.id.sport)
            list.add(R.id.date)
            list.add(R.id.time)
            list.add(R.id.place)

            val validation = Validation()
            if(validation.inputValidation(view, requireContext(), list)) {
                validation.createSuccessToast("Event is updated successfully", requireContext())
                findNavController().navigate(R.id.action_view_to_homeScreen)
            }
        }
        view.findViewById<Button>(R.id.cancel).setOnClickListener{
            findNavController().navigate(R.id.action_view_to_homeScreen)
        }
    }

}