package kr.co.calmme.model

import java.io.Serializable

data class CheckList(
    var error: String,
    var list: List<Challenge>
)

data class Challenge(
    var Id: Int,
    var Name: String,
    var Image: String,
    var Category: String,
    var Recommend: String,
    var Total: Int,
    var CreatedAt: String,
    var completeNum: Int
) :Serializable
