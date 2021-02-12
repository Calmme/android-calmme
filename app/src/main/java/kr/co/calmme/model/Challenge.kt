package kr.co.calmme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Challenge(
    var Id: Long,
    var Name: String ,
    var Image: String,
    var Category: String,
    var Recommend: Boolean,
    var CreatedAt: String,
   // var totalDay: Int,
    //var completeNum: Int
    ) : Serializable {
    init {
        // 초기화 코드 작성
    }
}

class CheckList(
    @SerializedName("elements")
    val checkRoomList : List<Challenge>
)