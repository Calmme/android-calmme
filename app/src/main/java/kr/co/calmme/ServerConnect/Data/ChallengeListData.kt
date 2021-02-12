package kr.co.calmme.ServerConnect

data class ChallengeListData(
    var error: String,
    var list: List<Items>
)

data class Items(
    var Id: Int,
    var Name: String,
    var Image: String,
    var Category: String,
    var Recommend: String,
    var CreatedAt: String
)
