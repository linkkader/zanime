package com.zanime.link.database


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TopRepository
    val allWords: LiveData<List<Top>>
    init {
        val libralyDao = TopRoomDatabase.getDatabase(application, viewModelScope).libraryDao()
        repository = TopRepository(libralyDao)
        allWords = repository.allWords
    }
    fun insert(libraly: Top) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(libraly)
    }
    fun deleteAll () = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}
