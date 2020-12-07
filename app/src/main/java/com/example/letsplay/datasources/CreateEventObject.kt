package com.example.letsplay.datasources

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import com.example.letsplay.R
import com.example.letsplay.models.EventInfo
import com.example.letsplay.models.LoggedInUserDtls
import com.google.android.material.textfield.TextInputLayout
import java.sql.Timestamp

class CreateEventObject {

    public fun createEventFromView(view : View, date : String?, startTime : String?) : EventInfo{

        val comments = view.findViewById<EditText>(R.id.comments).text.toString()
        val sport = view.findViewById<Spinner>(R.id.sport)
        val sportStr = sport.getItemAtPosition(sport.selectedItemPosition).toString()
        val place = view.findViewById<TextInputLayout>(R.id.place).editText?.text.toString()
        var timeStamp = System.currentTimeMillis()
        var id = sportStr + "_" + timeStamp
        val createdUserEmail = LoggedInUserDtls.loggedInUser?.emailAddress
        val createdUserName = LoggedInUserDtls.loggedInUser?.userName

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
            null
        )
    }

    public fun createEventFromDocData(docData: MutableMap<String, Any>) : EventInfo{

        var comments : String? = null
        var sport : String? = null
        var date : String? = null
        var time : String? = null
        var place : String? = null
        var createdByEmailAddress : String? = null
        var createdByUserName : String? = null
        var joinedBy : List<Any?>? = null
        var rejectedBy : List<Any?>? = null
        var id = ""

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
                it.key.equals("joinedBy") -> joinedBy = listOf(it.value)
                it.key.equals("rejectedBy") -> rejectedBy = listOf(it.value)
            }
        }

        return EventInfo(
            id, sport, date, time, place, comments, createdByEmailAddress, createdByUserName,
            joinedBy as List<String?>?, rejectedBy as List<String?>?
        )
    }

}