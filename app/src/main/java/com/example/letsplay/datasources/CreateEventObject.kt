package com.example.letsplay.datasources

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.letsplay.R
import com.example.letsplay.models.EventInfo
import com.example.letsplay.models.SessionDtls
import com.google.android.material.textfield.TextInputLayout
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CreateEventObject {

    public fun createEventFromView(view : View, date : String?, startTime : String?,
            timeStamp : Timestamp, placeLat : String, placeLon : String) : EventInfo{

        val comments = view.findViewById<EditText>(R.id.comments).text.toString()
        val sport = view.findViewById<Spinner>(R.id.sport)
        val sportStr = sport.getItemAtPosition(sport.selectedItemPosition).toString()
        var id = sportStr + "_" + timeStamp
        val createdUserEmail = SessionDtls.loggedInUser?.emailAddress
        val createdUserName = SessionDtls.loggedInUser?.userName
        val place = view.findViewById<TextInputLayout>(R.id.place).editText?.text.toString()
        val eventTimeStamp = createEventTimeStamp(date, startTime)

        return EventInfo(
            id,
            sportStr,
            date,
            startTime,
            placeLat,
            placeLon,
            place,
            comments,
            createdUserEmail,
            createdUserName,
            null,
            null,
            timeStamp,
            eventTimeStamp
        )
    }

    fun createEventTimeStamp(date : String?, time : String?) : Timestamp{
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        var eventDate = dateFormat.parse(date)
        val timeFormat = SimpleDateFormat("hh:mm a")
        var eventTime = timeFormat.parse(time)
        var eventDateTime = Date(eventDate.year, eventDate.month, eventDate.date, eventTime.hours, eventTime.minutes)
        var eventTimeStamp = Timestamp(eventDateTime.time)
        var eventDateTimeAgain = Date(eventTimeStamp.time)
        System.out.println(eventDateTimeAgain)
        return eventTimeStamp
    }

    fun createEventFromDocData(docData: MutableMap<String, Any>) : EventInfo{

        var comments : String? = null
        var sport : String? = null
        var date : String? = null
        var time : String? = null
        var placeLat : String? = null
        var placeLon : String? = null
        var place : String? = null
        var createdByEmailAddress : String? = null
        var createdByUserName : String? = null
        var interestedList : String? = null
        var declinedList : String? = null
        var id = ""
        var timeStampF = com.google.firebase.Timestamp(Date())
        var eventTimeStampF = com.google.firebase.Timestamp(Date())
        var timeStamp = Timestamp(System.currentTimeMillis())
        var eventTimeStamp = Timestamp(System.currentTimeMillis())

        docData.forEach { it ->
            when {
                it.key.equals("comments") -> comments = it.value.toString()
                it.key.equals("sport") -> sport = it.value.toString()
                it.key.equals("date") -> date = it.value.toString()
                it.key.equals("time") -> time = it.value.toString()
                it.key.equals("placeLat") -> placeLat = it.value.toString()
                it.key.equals("placeLon") -> placeLon = it.value.toString()
                it.key.equals("place") -> place = it.value.toString()
                it.key.equals("id") -> id = it.value as String
                it.key.equals("createdByEmailAddress") -> createdByEmailAddress = it.value.toString()
                it.key.equals("createdByUserName") -> {
                    if(it.value != null) {
                        createdByUserName = it.value.toString()
                    }
                }
                it.key.equals("interestedList") -> {
                    if(it.value != null) {
                        interestedList = it.value as String
                    }
                }
                it.key.equals("declinedList") -> {
                    if(it.value != null) {
                        declinedList = it.value as String
                    }
                }
                it.key.equals("timeStamp") -> {
                    timeStampF = it.value as com.google.firebase.Timestamp
                    val timeStampDate = timeStampF.toDate()
                    timeStamp = Timestamp(timeStampDate.time)

                }
                it.key.equals("eventTimeStamp") -> {
                    eventTimeStampF = it.value as com.google.firebase.Timestamp
                    val eventtimeStampDate = eventTimeStampF.toDate()
                    eventTimeStamp = Timestamp(eventtimeStampDate.time)
                }
            }
        }

        return EventInfo(
            id, sport, date, time, placeLat, placeLon, place, comments, createdByEmailAddress, createdByUserName,
            interestedList, declinedList, timeStamp, eventTimeStamp
        )
    }

}