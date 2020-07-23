
package com.zanime.link.database.search

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class TopRepository(private val libralyDao: TopDao) {
    val allWords: LiveData<List<Top>> = libralyDao.getAllLibraly()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(libray: Top) {
        libralyDao.insert(libray)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        libralyDao.deleteAll()
    }
}
