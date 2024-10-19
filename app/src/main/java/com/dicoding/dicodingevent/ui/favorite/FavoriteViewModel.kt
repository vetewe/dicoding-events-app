package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    val allFavorites: LiveData<List<FavoriteEvent>> = repository.getAllFavorites()
    val isLoading = MutableLiveData(true)

    init {
        allFavorites.observeForever {
            isLoading.value = false
        }
    }

    fun deleteFavoriteById(id: String) {
        viewModelScope.launch {
            repository.deleteFavoriteById(id)
        }
    }
}
