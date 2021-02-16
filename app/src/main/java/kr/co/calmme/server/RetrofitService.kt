package kr.co.calmme.server

import kr.co.calmme.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    /**
     * 1.이미지 파일 업로드
     */
//    @Multipart
//    @POST("upload")
//    fun uploadImage(@Part imgFile: MultipartBody.Part): Call<ImageRes>

    /**
     * 3. 방 생성
     */
    @POST("room")
    fun createRoom()

    /**
     * 4.모든 방 목록 조회
     */
    @GET("contents/list")
    fun onGoingChallengeList(): Call<CheckList>

    @GET("contents/detail")
    fun getChallengeDetail(
        @Query("id") id : Int
    ) : Call<ChallengeDetailList>
}