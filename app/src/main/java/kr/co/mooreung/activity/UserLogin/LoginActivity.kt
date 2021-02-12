package kr.co.mooreung.activity.UserLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*
import kr.co.mooreung.R
import kr.co.mooreung.ServerConnect.ChallengeListData
import kr.co.mooreung.ServerConnect.serverAPI
import kr.co.mooreung.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //로그인 공통 콜백 구성
        val callback: ((OAuthToken?, Throwable?) -> Unit) = { token, error ->
            if (error != null) { //Login Fail
                Log.e(TAG, "Kakao Login Failed :", error)
            } else if (token != null) { //Login Success

                startMainActivity()
            }
        }
        findViewById<ImageView>(R.id.kakao_login_btn).setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니라면 카카오계정으로 로그인
            LoginClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    loginWithKakaoTalk(this@LoginActivity, callback = callback)
                } else {
                    loginWithKakaoAccount(this@LoginActivity, callback = callback)
                }
            }
        }

        local_login_btn.setOnClickListener{
            val intent = Intent(this, LocalLoginActivity::class.java)
            startActivity(intent)
            //나중에 생명주기 고려해서 수정하면 될듯
            //finish()

        }

        testButton.setOnClickListener {
            val api = serverAPI.create()
            api.getChallengeList().enqueue(object  : Callback<ChallengeListData> {
                override fun onResponse(
                    call: Call<ChallengeListData>,
                    response: Response<ChallengeListData>
                ) {
                    Log.d("RESULT", "성공 : ${response.body()}")
                    Log.d("HTTP TEST", "http 프로토콜 성공")
                }
                override fun onFailure(call: Call<ChallengeListData>, t: Throwable) {
                    // 실패
                    Log.e("HTTP TEST", "FAIL")
                    Log.e("SEE", call.toString())
                }
            })
        }
    }


    private fun startMainActivity() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {

                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n생일: ${user.kakaoAccount?.birthday}" +
                        "\n성별: ${user.kakaoAccount?.gender}")
            }
        }
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

}