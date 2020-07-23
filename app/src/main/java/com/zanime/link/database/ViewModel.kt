package com.zanime.link.database


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zanime.link.type.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModel(application:  Application) : AndroidViewModel(application) {
    var mutableLists: MutableLiveData<ArrayList<Anime>>? = null
    var lists : ArrayList<Anime>? = null
    init {
        //populateList()
        mutableLists = MutableLiveData()
        lists = ArrayList()
        mutableLists!!.value = lists
    }
    fun insert(a: Anime)= viewModelScope.launch(Dispatchers.IO){
        lists!!.add(Anime())
    }
}
