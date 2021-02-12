package kr.co.calmme.server

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

//static 싱글톤
object Retrofit {
    //BASE URL
//    private const val BASE_URL_TEST = "http://calmme.kr:3000/"
    private const val BASE_URL_TEST = "http://codejune.iptime.org:3000/"

    //api 통신
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_TEST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}