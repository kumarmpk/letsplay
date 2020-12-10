package com.example.letsplay.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.letsplay.R
import com.example.letsplay.models.EventInfo
import com.example.letsplay.models.SessionDtls
import androidx.navigation.fragment.findNavController
import com.example.letsplay.fragments.ShowAllEvents
import com.example.letsplay.fragments.ViewEvent

class EventsRecyclerAdapter(private val events : List<EventInfo>,
                            private val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    init {
        this.listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.events_list_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position], listener)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val eventSport = itemView.findViewById<TextView>(R.id.eventSportTextView)
        val eventDate = itemView.findViewById<TextView>(R.id.eventDateTextView)
        val eventTime = itemView.findViewById<TextView>(R.id.eventTimeTextView)
        val eventPlace = itemView.findViewById<TextView>(R.id.eventPlaceTextView)

        fun bind(event : EventInfo, listener: OnItemClickListener?){
            eventSport.text = event.sport
            eventDate.text = event.date
            eventTime.text = event.time
            if(event.place == null || event.place!!.trim() == ""){
                eventPlace.text = "Link is provided inside."
            } else{
                eventPlace.text = event.place
            }

            itemView.setOnClickListener { listener!!.onItemClick(event) }
        }
    }

}

interface OnItemClickListener {
    fun onItemClick(item: EventInfo?)
}