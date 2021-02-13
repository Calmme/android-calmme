package kr.co.calmme.localDB.OnGoingChallenge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OngoingChallenge::class], version = 1, exportSchema = false)
abstract class OngoingChallengeDatabase : RoomDatabase() {
    abstract fun ongoingChallengeDao(): OngoingChallengeDao

    companion object {
        private var instance: OngoingChallengeDatabase? = null

        @Synchronized
        fun getInstance(context: Context): OngoingChallengeDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    OngoingChallengeDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}