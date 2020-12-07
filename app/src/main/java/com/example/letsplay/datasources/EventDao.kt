package com.example.letsplay.datasources

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsplay.adapters.EventsRecyclerAdapter
import com.example.letsplay.common.Constants
import com.example.letsplay.models.EventInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_show_all_events.*

class EventDao {

    public fun loadAllEvents(eventsRecyclerView : RecyclerView, activity : FragmentActivity?){

        val createEvent = CreateEventObject()
        var events : MutableList<EventInfo> = ArrayList()

        FirebaseFirestore.getInstance().collection(Constants.EVENTS)
            .addSnapshotListener { querySnapShot,firebaseFirestoreException ->
                querySnapShot?.let {
                    if(!it.isEmpty){
                        for(doc in it){
                            val eventInfo = createEvent.createEventFromDocData(doc.data)
                            events.add(eventInfo)
                        }
                        eventsRecyclerView.layoutManager = LinearLayoutManager(activity)
                        eventsRecyclerView.adapter = EventsRecyclerAdapter(events)
                    }
                }
            }

    }


}