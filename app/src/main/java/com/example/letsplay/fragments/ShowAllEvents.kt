package com.example.letsplay.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsplay.R
import com.example.letsplay.adapters.EventsRecyclerAdapter
import com.example.letsplay.adapters.OnItemClickListener
import com.example.letsplay.common.Constants
import com.example.letsplay.common.Validation
import com.example.letsplay.datasources.CreateEventObject
import com.example.letsplay.datasources.EventDao
import com.example.letsplay.models.EventInfo
import com.example.letsplay.models.SessionDtls
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_show_all_events.*
import java.lang.String
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

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

        loadAllEvents(eventsRecyclerView, activity)

        events_title.setOnMenuItemClickListener { menuItem ->
            titleMenuNavigator(menuItem)
        }

    }

    fun loadAllEvents(eventsRecyclerView: RecyclerView, activity: FragmentActivity?){

        val createEvent = CreateEventObject()
        var events : MutableList<EventInfo> = ArrayList()

        FirebaseFirestore.getInstance().collection(Constants.EVENTS)
            .addSnapshotListener { querySnapShot, firebaseFirestoreException ->
                querySnapShot?.let {
                    if(!it.isEmpty){
                        for(doc in it){

                            var eventTimeStamp = com.google.firebase.Timestamp(Date())

                            doc.data.forEach { it ->
                                when {
                                    it.key.equals("eventTimeStamp") -> eventTimeStamp = it.value as com.google.firebase.Timestamp
                                }
                            }
                            var eventTimeStampDate = eventTimeStamp.toDate()
                            var eventTimeStampConv = Timestamp(eventTimeStampDate.time)
                            val currentTimeStamp = Timestamp(System.currentTimeMillis())
                            if(currentTimeStamp < eventTimeStampConv){
                                val eventInfo = createEvent.createEventFromDocData(doc.data)
                                events.add(eventInfo)
                            }

                        }
                        if(events != null){
                            Collections.sort(events)
                        }
                        eventsRecyclerView.layoutManager = LinearLayoutManager(activity)
                        eventsRecyclerView.adapter = EventsRecyclerAdapter(
                            events,
                            object : OnItemClickListener {
                                override fun onItemClick(item: EventInfo?) {
                                    createViewAlert(item)
                                }
                            })
                    }
                }
            }
    }

    fun createViewAlert(event: EventInfo?){

        var view = layoutInflater.inflate(R.layout.fragment_view, null)

        view.findViewById<MaterialToolbar>(R.id.view_title).setTitle(event?.sport)
        view.findViewById<TextInputLayout>(R.id.date).editText?.setText(event?.date)
        view.findViewById<TextInputLayout>(R.id.date).editText?.setEnabled(false)
        view.findViewById<TextInputLayout>(R.id.time).editText?.setText(event?.time)
        view.findViewById<TextInputLayout>(R.id.time).editText?.setEnabled(false)
        view.findViewById<TextInputLayout>(R.id.place).editText?.setText(event?.place)
        view.findViewById<EditText>(R.id.comments).setText(event?.comments)

        if(event != null && event.interestedList != null){
            var interestedListArr = event.interestedList!!.split(",")
            view.findViewById<TextView>(R.id.interestedCount).setText(interestedListArr.size.toString())
        }

        if(event != null && event.declinedList != null){
            var declinedListArr = event.declinedList!!.split(",")
            view.findViewById<TextView>(R.id.declinedCount).setText(declinedListArr.size.toString())
        }

        val builder = AlertDialog.Builder(requireContext())

        builder.setView(view)

        builder.setPositiveButton(("Join"), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, id: Int) {
                if(event != null && event.interestedList != null){
                    event.interestedList = event.interestedList + "," +(SessionDtls.loggedInUser?.emailAddress)
                } else {
                    event?.interestedList = (SessionDtls.loggedInUser?.emailAddress)
                }

                event?.comments = view.findViewById<EditText>(R.id.comments).text.toString()

                val eventDao = EventDao()
                eventDao.saveEvent(
                    event, requireContext(), findNavController(),
                    "Event updated successfully.", R.id.action_showAllEvents_self
                )
                findNavController().navigate(R.id.action_showAllEvents_self)
            }
        })

        builder.setNegativeButton(("Reject"), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, id: Int) {
                if(event != null && event.declinedList != null){
                    event?.declinedList = event?.declinedList + "," +(SessionDtls.loggedInUser?.emailAddress)
                } else {
                    event?.declinedList = (SessionDtls.loggedInUser?.emailAddress)
                }

                event?.comments = view.findViewById<EditText>(R.id.comments).text.toString()

                val eventDao = EventDao()
                eventDao.saveEvent(
                    event, requireContext(), findNavController(),
                    "Event updated successfully.", R.id.action_showAllEvents_self
                )
                findNavController().navigate(R.id.action_showAllEvents_self)
            }
        })

        builder.setNeutralButton(("Cancel"), object : DialogInterface.OnClickListener {
            override fun onClick(dialog : DialogInterface?, id: Int) {

            }
        })

        view.findViewById<Button>(R.id.placeBtn).setOnClickListener{

            if(event != null && event.placeLat != null && event.placeLon != null){
                var uri = "http://maps.google.com/maps?daddr=" + event.placeLat + ", " + event.placeLon
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            } else{
                val validation = Validation()
                validation.createErrorToast(
                    "The user did not provide GPS location." +
                            "\n Please use the place text box to get the location.",
                    requireContext()
                )
            }

        }

        val alert = builder.create()
        alert.show()

    }

    private fun titleMenuNavigator(menuItem: MenuItem) : Boolean{
        when(menuItem.itemId){
            R.id.home_menu -> {
                findNavController().navigate(R.id.action_showAllEvents_to_homeScreen)
                return true
            }
            R.id.create_menu -> {
                findNavController().navigate(R.id.action_showAllEvents_to_create)
                return true
            }
            R.id.view_join_menu -> {
                findNavController().navigate(R.id.action_showAllEvents_self)
                return true
            }
            R.id.update_profile_menu -> {
                findNavController().navigate(R.id.action_showAllEvents_to_profile)
                return true
            }
            R.id.log_out_menu -> {
                SessionDtls.loggedInUser = null
                findNavController().navigate(R.id.action_showAllEvents_to_signIn)
                return true
            }
            else -> return false
        }
    }



}