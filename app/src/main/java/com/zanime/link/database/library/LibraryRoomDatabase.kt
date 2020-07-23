package com.zanime.link.database.library


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Library::class], version = 1)
abstract class LibraryRoomDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
    companion object {
        @Volatile
        private var INSTANCE: LibraryRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): LibraryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, LibraryRoomDatabase::class.java, "library_table7")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
