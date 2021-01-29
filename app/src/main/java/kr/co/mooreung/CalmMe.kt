package kr.co.mooreung

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class CalmMe : Application(){

    override fun onCreate() {
        super.onCreate()
        //CalmMe Hash key 받아오기
        val keyHash = Utility.getKeyHash(this)
        Log.d("KEY_HASH", keyHash)
        //카카오 SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}