package com.zanime.link.database.history


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistoryRepository
    val all: LiveData<List<History>>
    init {
        val dao = HistoryRoomDatabase.getDatabase(application, viewModelScope).libraryDao()
        repository = HistoryRepository(dao)
        all = repository.allHistorys
    }
    fun insert(history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(history)
    }
    fun deleteAll () = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
    fun remove (source: String,name: String ) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(source,name)
    }
}
