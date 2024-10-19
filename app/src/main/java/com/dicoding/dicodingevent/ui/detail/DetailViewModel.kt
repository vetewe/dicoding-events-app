package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import android.util.Log

class DetailViewModel(private val repository: EventRepository) : ViewModel() {
    private val _eventDetail = MutableLiveData<ListEventsItem?>()
    val eventDetail: LiveData<ListEventsItem?> = _eventDetail

    fun getEventDetail(eventId: Int) {
        val client = ApiConfig.getApiService().getEvent(id = eventId.toString())
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.listEvents?.firstOrNull { it?.id == eventId }?.let { event ->
                        if (event.name != null && event.imageLogo != null) {
                            _eventDetail.value = event
                        }
                    }
                } else {
                    _eventDetail.value = null
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _eventDetail.value = null
            }
        })
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
            Log.d("DetailViewModel", "Attempting to delete favorite")
            repository.deleteFavoriteById(id)
            Log.d("DetailViewModel", "Favorite deleted")
        }
    }
}
