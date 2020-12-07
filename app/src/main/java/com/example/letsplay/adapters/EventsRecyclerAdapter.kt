package com.example.letsplay.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letsplay.R
import com.example.letsplay.models.EventInfo

class EventsRecyclerAdapter(private val events : List<EventInfo>) : RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.events_list_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event : EventInfo = events[position]
        holder.eventSport?.text = event.sport
        holder.eventDate?.text = event.date
        holder.eventTime?.text = event.time
        holder.eventPlace?.text = event.place
        holder.eventPosition = position
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val eventSport = itemView.findViewById<TextView>(R.id.eventSportTextView)
        val eventDate = itemView.findViewById<TextView>(R.id.eventDateTextView)
        val eventTime = itemView.findViewById<TextView>(R.id.eventTimeTextView)
        val eventPlace = itemView.findViewById<TextView>(R.id.eventPlaceTextView)
        var eventPosition = 0
    }

}