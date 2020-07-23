
package com.zanime.link.database.library

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class LibraryRepository(private val libralyDao: LibraryDao) {
    val allLibrarys: LiveData<List<Library>> = libralyDao.getAllLibraly()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLibrary(source:String,name: String):LiveData<List<Library>> {
        return libralyDao.getLibraly(source,name)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(libray: Library) {
        libralyDao.insert(libray)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(source:String,name: String) {
        libralyDao.delete(source,name)
    }
}
