package com.example.letsplay.models

data class LetsPlayUser(
    var emailAddress : String?,
    var password : String,
    val securityQuestion : String,
    val securityAnswer : String,
    val sport1 : String,
    val sport2 : String,
    val sport3 : String,
    var userName : String,
    var newUser : Boolean
)