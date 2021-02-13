package kr.co.calmme.localDB

import androidx.room.*
import kr.co.calmme.model.Challenge

@Entity(tableName = "tb_my_challenge")
data class MyChallenge(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var challenge: Challenge
)
