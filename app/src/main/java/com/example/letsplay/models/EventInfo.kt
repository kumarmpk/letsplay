package com.example.letsplay.models

data class EventInfo(
    var id: String,
    var sport: String?,
    var date: String?,
    var time: String?,
    var place: String?,
    var comments: String?,
    var createdByEmailAddress:String?,
    var createdByUserName:String?,
    var joinedBy: List<String?>?,
    var rejectedBy:List<String?>?
)