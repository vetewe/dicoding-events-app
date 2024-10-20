package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {
    private val _eventDetail = MutableLiveData<ListEventsItem?>()
    val eventDetail: LiveData<ListEventsItem?> = _eventDetail

    fun getEventDetail(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvent(id = eventId.toString())
                val event = response.listEvents?.firstOrNull { it?.id == eventId }
                _eventDetail.postValue(event)
            } catch (e: Exception) {
                _eventDetail.postValue(null)
            }
        }
    }

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> {
        return repository.getFavoriteEventById(id)
    }

    fun insertFavorite(event: FavoriteEvent) {
        if (event.id.isNotEmpty() && event.name.isNotEmpty()) {
            viewModelScope.launch {
                repository.insertFavorite(event)
            }
        }
    }

    fun deleteFavoriteById(id: String) {
        viewModelScope.launch {
            repository.deleteFavoriteById(id)
        }
    }
}
