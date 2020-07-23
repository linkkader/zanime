
package com.zanime.link.database.history

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class HistoryRepository(private val historyDao: HistoryDao) {
    val allHistorys: LiveData<List<History>> = historyDao.getAllHistorys()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(history: History) {
        historyDao.insert(history)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        historyDao.deleteAll()
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(source:String,name: String) {
        historyDao.remove(source,name)
    }
}
