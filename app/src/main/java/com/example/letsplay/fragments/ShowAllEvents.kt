package com.example.letsplay.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.letsplay.R
import com.example.letsplay.datasources.EventDao
import com.example.letsplay.models.EventInfo
import com.example.letsplay.models.LoggedInUserDtls
import kotlinx.android.synthetic.main.fragment_show_all_events.*

class ShowAllEvents : Fragment() {

    private var events :MutableList<EventInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_all_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventDao = EventDao()
        eventDao.loadAllEvents(eventsRecyclerView, activity)

        events_title.setOnMenuItemClickListener {menuItem ->
            titleMenuNavigator(menuItem)
        }


    }

    private fun titleMenuNavigator(menuItem : MenuItem) : Boolean{
        when(menuItem.itemId){
            R.id.create_menu -> {
                findNavController().navigate(R.id.action_homeScreen_to_create)
                return true
            }
            R.id.view_join_menu -> {
                findNavController().navigate(R.id.action_homeScreen_to_showAllEvents)
                return true
            }
            R.id.update_profile_menu -> {
                findNavController().navigate(R.id.action_homeScreen_to_profile)
                return true
            }
            R.id.log_out_menu -> {
                LoggedInUserDtls.userName = ""
                LoggedInUserDtls.emailAddress = ""
                LoggedInUserDtls.isLoggedIn = false
                findNavController().navigate(R.id.action_homeScreen_to_signIn)
                return true
            }
            else -> return false
        }
    }



}