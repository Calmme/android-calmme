package kr.co.mooreung.ServerConnect

import kr.co.mooreung.ServerConnect.Data.UserData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface serverAPI {

    @FormUrlEncoded
    @POST("/auth/join")
    fun userSignin(
        @Field("userEmail") userEmail: String,
        @Field("userPassword") userPassword: String,
        @Field("userNickname") userNickname: String,
        @Field("userAge") userAge: String,
    ): Call<UserData>

    @GET("/contents/list")
    fun getChallengeList(
    ): Call<ChallengeListData>


    companion object {
        //원래 안드로이드에서 http 통신은 자제적으로 제한하고 있지만
        //현재 서버의 ssl 설정이 되어 있지 않으므로 res/xml/network_security_cofing 파일 설정을 통해서
        //임시로 calmme.kr 도메인만 http 통신을 허용해놓음
        private const val BASE_URL_API = "http://calmme.kr:3000"
        fun create(): serverAPI {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()

                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(serverAPI::class.java)
        }
    }
}