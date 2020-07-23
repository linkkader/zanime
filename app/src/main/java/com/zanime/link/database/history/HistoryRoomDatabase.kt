package com.zanime.link.database.history


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [History::class], version = 1)
abstract class HistoryRoomDatabase : RoomDatabase() {
    abstract fun libraryDao(): HistoryDao
    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): HistoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, HistoryRoomDatabase::class.java, "history3")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
