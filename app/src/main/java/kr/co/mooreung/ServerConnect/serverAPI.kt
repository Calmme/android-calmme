package kr.co.mooreung.ServerConnect

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface serverAPI {

    @FormUrlEncoded
    @POST("/test")
    fun postTestData(
        @Field("testData") testDate: String
    ): Call<ResultTest>

    @FormUrlEncoded
    @POST("/auth")
    fun userSignin(
        @Field("userEmail") userEmail: String,
        @Field("userPassword") userPassword: String,
        @Field("userNickname") userNickname: String
    ): Call<UserData>


    companion object {
        private const val BASE_URL_API = "http://codejune.iptime.org:3000"
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