package kr.co.mooreung.activity.UserLogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_local_login.*
import kr.co.mooreung.R
import kr.co.mooreung.ServerConnect.Data.UserData
import kr.co.mooreung.ServerConnect.serverAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocalLoginActivity : AppCompatActivity() {

    var emailText: EditText? = null
    var passwordText: EditText? = null
    var reEnterPasswordText: EditText? = null
    var nicknameText: EditText? = null
    var ageText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_login)

        emailText = findViewById<EditText>(R.id.user_email)
        passwordText = findViewById<EditText>(R.id.user_password)
        reEnterPasswordText = findViewById<EditText>(R.id.user_password_re)
        nicknameText = findViewById<EditText>(R.id.user_nickname)
        ageText = findViewById<EditText>(R.id.user_age)

       signupButton.setOnClickListener {
           signup()
       }

    }
    fun onSignupSuccess() {
        signupButton!!.isEnabled = true
//        setResult(Activity.RESULT_OK, null)
//        finish()
       // startActivity(Intent(this, MainActivity::class.java))
        //finish()
    }

    fun onSignupFailed() {
        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_LONG).show()

        signupButton!!.isEnabled = true
    }
    fun signup() {
        Log.d(TAG, "회원가입")

        if (!validate()) {
            //미입력 정보 및 형식에 맞지 않은 정보가 있을 시 로그인 실패
            onSignupFailed()
            return
        }

        signupButton!!.isEnabled = false

        val email = emailText!!.text.toString()
        val password = passwordText!!.text.toString()
       // val reEnterPassword = reEnterPasswordText!!.text.toString()
        val nickname = nicknameText!!.text.toString()
        val age = ageText!!.text.toString()

        // TODO: Implement your own signup logic here.

        val api = serverAPI.create()
        api.userSignin(email, password, nickname, age).enqueue(object : Callback<UserData> {
            override fun onResponse(
                call: Call<UserData>,
                response: Response<UserData>
            ) {
                Log.d("RESULT", "성공 : ${response}")
                Log.d("HTTP TEST", "http 프로토콜 성공")
                onSignupSuccess()

            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                // 실패
                Log.e("HTTP TEST", "FAIL")
                Log.e("SEE", call.toString())
                onSignupFailed()
            }
        })


    }

    //사용자가 입력한 정보가 유효한 정보인지 확인
    fun validate(): Boolean {
        var valid = true

        val email = emailText!!.text.toString()
        val password = passwordText!!.text.toString()
        val reEnterPassword = reEnterPasswordText!!.text.toString()
        val nickname = nicknameText!!.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText!!.error = "enter a valid email address"
            valid = false
        } else {
            emailText!!.error = null
        }


        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            passwordText!!.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            passwordText!!.error = null
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length < 4 || reEnterPassword.length > 10 || reEnterPassword != password) {
            reEnterPasswordText!!.error = "Password Do not match"
            valid = false
        } else {
            reEnterPasswordText!!.error = null
        }

        if (nickname.isEmpty() || nickname.length < 3) {
            nicknameText!!.error = "at least 3 characters"
            valid = false
        } else {
            nicknameText!!.error = null
        }
        return valid
    }

    companion object {
        private val TAG = "SignupActivity"
    }
}