package kr.co.mooreung.activity


import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

import kr.co.mooreung.R
import kr.co.mooreung.ServerConnect.NaverAPI
import kr.co.mooreung.ServerConnect.ResultGetSearchNews
import kr.co.mooreung.ServerConnect.ResultTest
import kr.co.mooreung.ServerConnect.testAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        testButton.setOnClickListener {
            val api = testAPI.create()

            api.postTestData("이거 넘어가면 POST 성공").enqueue(object : Callback<ResultTest> {
                override fun onResponse(
                    call: Call<ResultTest>,
                    response: Response<ResultTest>
                ) {
                    Log.d("RESULT", "성공 : ${response.body()}")
                    Log.e("HTTP TEST", "http 프로토콜 성공")
                }
                override fun onFailure(call: Call<ResultTest>, t: Throwable) {
                    // 실패
                    Log.e("HTTP TEST", "FAILFAILFAILFAIL")
                    Log.e("SEE", call.toString())
                }
            })
        }

    }



}