package kr.co.calmme.localDB.OnGoingChallenge

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OngoingChallengeDao {
    @Query("SELECT * FROM tb_ongoing_challenge")
    fun getAll(): List<OngoingChallenge>

    @Insert
    fun insertAll(vararg contacts: OngoingChallenge)

    @Delete
    fun delete(contacts: OngoingChallenge)
}