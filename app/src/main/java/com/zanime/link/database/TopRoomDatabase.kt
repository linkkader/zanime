package com.zanime.link.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Top::class], version = 1)
abstract class TopRoomDatabase : RoomDatabase() {
    abstract fun libraryDao(): TopDao
    companion object {
        @Volatile
        private var INSTANCE: TopRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): TopRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, TopRoomDatabase::class.java, "top1")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
