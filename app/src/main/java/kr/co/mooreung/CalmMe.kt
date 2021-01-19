package kr.co.mooreung

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class CalmMe : Application(){

    override fun onCreate() {
        super.onCreate()
        //카카오 SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}