package com.example.letsplay.datasources

import android.content.Context
import androidx.navigation.NavController
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.common.Validation
import com.example.letsplay.models.EventInfo
import com.google.firebase.firestore.FirebaseFirestore

class EventDao {

    fun saveEvent(eventInfo : EventInfo?, context : Context, findNavController : NavController,
        successMsg : String, navigationId : Int) {

        val validation = Validation()

        if (eventInfo != null) {
            FirebaseFirestore.getInstance().collection(Constants.EVENTS)
                .document(eventInfo.id).set(eventInfo)
                .addOnSuccessListener {
                    validation.createSuccessToast(successMsg, context)
                    findNavController.navigate(navigationId)
                }.addOnFailureListener {
                    validation.createErrorToast(
                        "Application faced unexpected Exception. Kindly contact support team",
                        context
                    )
                }

        }
    }

}