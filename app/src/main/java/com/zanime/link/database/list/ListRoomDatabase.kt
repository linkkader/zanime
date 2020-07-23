package com.zanime.link.database.list


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [List1::class], version = 1)
abstract class ListRoomDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    companion object {
        @Volatile
        private var INSTANCE: ListRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): ListRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ListRoomDatabase::class.java, "list_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.listDao())
                    }
                }
            }
        }
        fun populateDatabase(listDao: ListDao) {
            listDao.deleteAll()
         }
    }
}
