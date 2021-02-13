package kr.co.calmme.localDB.MyChallenge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyChallenge::class], version = 1, exportSchema = false)
abstract class MyChallengeDatabase : RoomDatabase() {
    abstract fun myChallengeDao(): MyChallengeDao

    companion object {
        private var instance: MyChallengeDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MyChallengeDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyChallengeDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}