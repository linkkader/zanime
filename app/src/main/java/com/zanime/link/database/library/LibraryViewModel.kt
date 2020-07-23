package com.zanime.link.database.library


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LibraryRepository
    val allWords: LiveData<List<Library>>
    init {
        val libralyDao = LibraryRoomDatabase.getDatabase(application, viewModelScope).libraryDao()
        repository = LibraryRepository(libralyDao)

        allWords = repository.allLibrarys
    }
    fun insert(libraly: Library) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(libraly)
    }
    fun delete (source: String,name: String ) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(source,name)
    }
}
