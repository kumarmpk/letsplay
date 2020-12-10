import androidx.appcompat.widget.AppCompatSpinner
import com.example.letsplay.R
import com.example.letsplay.common.Constants
import com.example.letsplay.models.LetsPlayUser
import com.example.letsplay.models.SessionDtls
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentSnapshot
import android.view.View

class CreateUserObject{

    public fun createUserObjectFromDocumentSnapShot (document : DocumentSnapshot) : LetsPlayUser {

        var emailAddress = ""
        var password = ""
        var securityAnswer = ""
        var securityQuestion = ""
        var sport1 = ""
        var sport2 = ""
        var sport3 = ""
        var userName = ""

        document?.data?.forEach {item ->
            when (item.key){
                Constants.EMAIL_ADDRESS -> emailAddress = item.value as String
                Constants.password -> password = item.value as String
                Constants.securityQuestion -> securityQuestion = item.value as String
                Constants.securityAnswer -> securityAnswer = item.value as String
                Constants.sport1 -> sport1 = item.value as String
                Constants.sport2 -> sport2 = item.value as String
                Constants.sport3 -> sport3 = item.value as String
                Constants.userName -> userName = item.value as String
            }
        }

        return LetsPlayUser(
            emailAddress, password, securityQuestion, securityAnswer,
            sport1, sport2, sport3, userName, false
        )
    }

    public fun createUserObjectFromView( view : View ) : LetsPlayUser{

        val userName = view.findViewById<TextInputLayout>(R.id.userNameTextLayout).editText?.text.toString()
        SessionDtls.loggedInUser?.userName = userName
        val emailStr = view.findViewById<TextInputLayout>(R.id.emailAddressTextLayout).editText?.text.toString()
        val password = view.findViewById<TextInputLayout>(R.id.password).editText?.text.toString()
        val securityQuestion = view.findViewById<AppCompatSpinner>(R.id.securityQuestion)
        val securityQuestionStr = securityQuestion.getItemAtPosition(securityQuestion.selectedItemPosition).toString()
        val securityAnswer = view.findViewById<TextInputLayout>(R.id.securityAnswer).editText?.text.toString()
        val sport1 = view.findViewById<AppCompatSpinner>(R.id.sport1)
        val sport1Str = sport1.getItemAtPosition(sport1.selectedItemPosition).toString()
        val sport2 = view.findViewById<AppCompatSpinner>(R.id.sport2)
        var sport2Str = sport2.getItemAtPosition(sport2.selectedItemPosition).toString()

        if(sport2Str.contains("Select a")){
            sport2Str = ""
        }

        val sport3 = view.findViewById<AppCompatSpinner>(R.id.sport3)
        var sport3Str = sport3.getItemAtPosition(sport3.selectedItemPosition).toString()

        if(sport3Str.contains("Select a")){
            sport3Str = ""
        }

        val newUser = true

        return LetsPlayUser(
            emailStr, password, securityQuestionStr, securityAnswer,
            sport1Str, sport2Str, sport3Str, userName, newUser
        )
    }

}