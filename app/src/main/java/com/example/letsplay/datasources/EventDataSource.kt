package com.example.letsplay.datasources

import com.example.letsplay.models.EventInfo
import java.util.*
import kotlin.collections.ArrayList

object EventDataSource {

    val events = ArrayList<EventInfo>()

    init {
        initializeEvents()
    }

    private fun initializeEvents(){
        var event = EventInfo(
            "1", "Cricket", "15 Nov 2020",
            "11:00", "Oval", null, "User1@as.as",
            "User1", Arrays.asList("User2", "User3"), Arrays.asList("User4"))
        events.add(event)

        event = EventInfo(
            "2","Soccer", "14 Nov 2020",
            "11:00", "Oval", "We got ball.", "User2@as.as",
            "User2", Arrays.asList("User3", "User4"), Arrays.asList("User5"))
        events.add(event)

        event = EventInfo(
            "3","Baseball", "15 Nov 2020",
            "23:00", "Oval", null, "User3@as.as",
            "User3", Arrays.asList("User4", "User5"), Arrays.asList("User6"))
        events.add(event)

        event = EventInfo(
            "4", "Tennis", "16 Nov 2020",
            "11:00", "Oval", "I have only one racquet.", "User2@as.as",
            "User2", Arrays.asList("User3", "User4"), Arrays.asList("User5"))
        events.add(event)

        event = EventInfo(
            "5","Badminton", "17 Nov 2020",
            "11:00", "Oval", null, "User5@as.as",
            "User5", Arrays.asList("User1", "User2"), Arrays.asList("User3"))
        events.add(event)

        event = EventInfo(
            "6","Football", "18 Nov 2020",
            "11:00", "Oval", null, "User2@as.as",
            "User2", Arrays.asList("User3", "User4"), Arrays.asList("User5"))
        events.add(event)

        event = EventInfo("7","Volleyball", "19 Nov 2020",
            "11:00", "Oval", null, "User1@as.as",
            "User1", Arrays.asList("User2", "User4"), Arrays.asList("User5"))
        events.add(event)
    }

}