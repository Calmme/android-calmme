package kr.co.mooreung.ServerConnect

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface testAPI {
    @GET("/")
    fun getTestData(
        /*
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null
         */
    ): Call<ResultGetSearchNews>

    @FormUrlEncoded
    @POST("")
    fun transferPapago(
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): Call<ResultTransferPapago>

    @FormUrlEncoded
    @POST("/")
    fun postTestData(
        @Field("testData") testDate: String
    ): Call<ResultTest>

    companion object {
        private const val BASE_URL_NAVER_API = "http://codejune.iptime.org:3000"

        fun create(): testAPI {
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
                .baseUrl(BASE_URL_NAVER_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(testAPI::class.java)
        }
    }
}