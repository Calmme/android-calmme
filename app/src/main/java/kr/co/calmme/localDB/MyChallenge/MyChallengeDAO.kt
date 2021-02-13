package kr.co.calmme.localDB.MyChallenge

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyChallengeDao {
    @Query("SELECT * FROM tb_my_challenge")
    fun getAll(): List<MyChallenge>

    @Insert
    fun insertAll(vararg contacts: MyChallenge)

    @Delete
    fun delete(contacts: MyChallenge)
}