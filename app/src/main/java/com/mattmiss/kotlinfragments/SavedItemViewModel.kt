package com.mattmiss.kotlinfragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedItemViewModel(application: Application) : AndroidViewModel(application){

    private val repository: SavedItemRepository
    val allSavedItems: LiveData<List<SavedItem>>

    init {
        val savedItemDao = SavedItemDatabase.getDatabase(application, viewModelScope).savedItemDao()
        repository = SavedItemRepository(savedItemDao)
        allSavedItems = repository.allSavedItems
    }

    fun insert(savedItem: SavedItem) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(savedItem)
    }

    fun delete(savedItem: SavedItem) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(savedItem)
    }
}

