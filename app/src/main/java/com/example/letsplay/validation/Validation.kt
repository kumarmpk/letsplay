package com.example.letsplay.validation

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class Validation {

    fun inputValidation(view : View, context : Context, list : List<Int>) : Boolean {

        var flag = true
        for(element in list) {

            val fieldValue : String = view.findViewById<TextInputLayout>(element).editText?.text.toString()
            val fieldName : String = view.findViewById<TextInputLayout>(element).editText?.hint.toString()

            if (TextUtils.isEmpty(fieldValue)) {
                createErrorToast("Invalid "+fieldName, context)
                flag = false
                break
            }
        }
        return flag
    }

    fun createErrorToast(variable: String, context: Context){
        val toast : Toast = Toast.makeText(context, variable, Toast.LENGTH_SHORT)
        val view : View = toast.view
        val text : TextView = view.findViewById(android.R.id.message)
        text.setTextColor(Color.RED)
        text.setBackgroundColor(Color.YELLOW)
        toast.show()
    }

    fun createSuccessToast(variable: String, context: Context){
        val toast : Toast = Toast.makeText(context, variable, Toast.LENGTH_SHORT)
        val view : View = toast.view
        val text : TextView = view.findViewById(android.R.id.message)
        text.setTextColor(Color.GREEN)
        text.setBackgroundColor(Color.YELLOW)
        toast.show()
    }

    fun isEmailValid(id: Int, view: View, context: Context): Boolean {
        val email : String = view.findViewById<TextInputLayout>(id).editText?.text.toString()
        var flag = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if(!flag){
            createErrorToast("Invalid Email Address", context)
        }
        return flag
    }

}