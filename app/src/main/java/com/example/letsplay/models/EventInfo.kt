package com.example.letsplay.models

import java.sql.Timestamp

data class EventInfo(
    var id: String,
    var sport: String?,
    var date: String?,
    var time: String?,
    var placeLat: String?,
    var placeLon: String?,
    var place: String?,
    var comments: String?,
    var createdByEmailAddress:String?,
    var createdByUserName:String?,
    var interestedList: String?,
    var declinedList: String?,
    var timeStamp : Timestamp,
    var eventTimeStamp : Timestamp
) : Comparable<EventInfo> {
    override fun compareTo(obj: EventInfo): Int {
        if(this.eventTimeStamp < obj.eventTimeStamp){
            return -1
        } else{
            return 1
        }
    }
}