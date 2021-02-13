package kr.co.calmme.localDB.MyChallenge

import androidx.room.*
import kr.co.calmme.model.Challenge

@Entity(tableName = "tb_my_challenge")
data class MyChallenge(
    @PrimaryKey
    var id: Long,
    var name: String,
    var Image: String,
    var Category: String,
    var Total: Int,
    var CreatedAt: String,
    var completeNum: Int,
)