package kr.co.calmme.model

data class ChallengeDetailList(
    val challenge : ChallengeDetail,
    val error : String
)
data class ChallengeDetail(
    var Name : String,
    var Total : Int,
    var SubName : String,
    var Image : String
)