package kr.co.calmme.model

import java.io.Serializable

class Challenge(
    var cId: Int,
    var title: String ,
    var image_url: String,
    var totalDay: Int,
    var completeNum: Int) : Serializable {
    init {
        // 초기화 코드 작성
    }
}