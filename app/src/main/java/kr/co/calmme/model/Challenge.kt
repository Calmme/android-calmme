package kr.co.calmme.model

import kr.co.calmme.localDB.MyChallenge.MyChallenge
import java.io.Serializable

data class CheckList(
    var error: String,
    var list: List<Challenge>,
)

data class Challenge(
    var Id: Int,
    var Name: String,
    var Image: String,
    var Category: String,
    var Recommend: String,
    var Total: Int,
    var CreatedAt: String,
    var completeNum: Int,
    var isScraped: Boolean,
) : Serializable {

    fun toMyChallenge(obj: MyChallenge) {
        obj.id = this.Id.toLong()
        obj.name = this.Name
        obj.Image = this.Image
        obj.Category = this.Category
        obj.Total = this.Total
        obj.CreatedAt = this.CreatedAt
        obj.completeNum = this.completeNum
    }
}
