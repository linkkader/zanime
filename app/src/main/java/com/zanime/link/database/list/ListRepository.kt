
package com.zanime.link.database.list

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ListRepository(private val listDao: ListDao) {
    val all: LiveData<List<List1>> = listDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(list: List1) {
        listDao.insert(list)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        listDao.deleteAll()
    }
}
