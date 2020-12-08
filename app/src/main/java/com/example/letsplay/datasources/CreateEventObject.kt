package com.example.letsplay.datasources

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import com.example.letsplay.R
import com.example.letsplay.models.EventInfo
import com.example.letsplay.models.SessionDtls
import com.google.android.material.textfield.TextInputLayout

class CreateEventObject {

    public fun createEventFromView(view : View, date : String?, startTime : String?,
            timeStamp : String) : EventInfo{

        val comments = view.findViewById<EditText>(R.id.comments).text.toString()
        val sport = view.findViewById<Spinner>(R.id.sport)
        val sportStr = sport.getItemAtPosition(sport.selectedItemPosition).toString()
        val place = view.findViewById<TextInputLayout>(R.id.place).editText?.text.toString()
        var id = sportStr + "_" + timeStamp
        val createdUserEmail = SessionDtls.loggedInUser?.emailAddress
        val createdUserName = SessionDtls.loggedInUser?.userName

        return EventInfo(
            id,
            sportStr,
            date,
            startTime,
            place,
            comments,
            createdUserEmail,
            createdUserName,
            null,
            null,
            timeStamp
        )
    }

    fun createEventFromDocData(docData: MutableMap<String, Any>) : EventInfo{

        var comments : String? = null
        var sport : String? = null
        var date : String? = null
        var time : String? = null
        var place : String? = null
        var createdByEmailAddress : String? = null
        var createdByUserName : String? = null
        var interestedList : String? = null
        var declinedList : String? = null
        var id = ""
        var timeStamp = ""

        docData.forEach { it ->
            when {
                it.key.equals("comments") -> comments = it.value.toString()
                it.key.equals("sport") -> sport = it.value.toString()
                it.key.equals("date") -> date = it.value.toString()
                it.key.equals("time") -> time = it.value.toString()
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
                it.key.equals("timeStamp") -> timeStamp = it.value as String
            }
        }

        return EventInfo(
            id, sport, date, time, place, comments, createdByEmailAddress, createdByUserName,
            interestedList, declinedList, timeStamp
        )
    }

}