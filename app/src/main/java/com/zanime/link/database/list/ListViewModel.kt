package com.zanime.link.database.list


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ListRepository
    val all: LiveData<List<List1>>
    init {
        val listDao = ListRoomDatabase.getDatabase(application, viewModelScope).listDao()
        repository = ListRepository(listDao)
        all = repository.all
    }
    fun insert(list: List1) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(list)
    }
    fun deleteAll () = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}
