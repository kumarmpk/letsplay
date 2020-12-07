package com.example.letsplay.common

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
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

    fun inputValidationSpinner(view : View, context : Context, list : List<Int>) : Boolean {

        var flag = true
        for(element in list) {

            val fieldValue = view.findViewById<AppCompatSpinner>(element)
            val fieldValueStr = fieldValue.getItemAtPosition(fieldValue.getSelectedItemPosition()).toString()

            if (TextUtils.isEmpty(fieldValueStr) || fieldValueStr.contains("Select a ")) {
                createErrorToast("Invalid value "+fieldValueStr, context)
                flag = false
                break
            }
        }
        return flag
    }

    fun inputValidationStr(view : View, context : Context, list : List<String?>) : Boolean {

        var flag = true
        for(element in list) {

            if (TextUtils.isEmpty(element)) {
                createErrorToast("Mandatory fields are empty. Please provide a valid value and try again.", context)
                flag = false
                break
            }
        }
        return flag
    }

    fun createErrorToast(variable: String, context: Context){
        val toast : Toast = Toast.makeText(context, variable, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun createSuccessToast(variable: String, context: Context){
        val toast : Toast = Toast.makeText(context, variable, Toast.LENGTH_SHORT)
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