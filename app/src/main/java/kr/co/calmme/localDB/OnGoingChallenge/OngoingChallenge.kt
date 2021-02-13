package kr.co.calmme.localDB.OnGoingChallenge

import androidx.room.*
import kr.co.calmme.model.Challenge

@Entity(tableName = "tb_ongoing_challenge")
data class OngoingChallenge(
    @PrimaryKey
    var id: Long,
    var name: String,
    var Image: String,
    var Category: String,
    var Total: Int,
    var CreatedAt: String,
    var completeNum: Int,
)