package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getEvents() {
        if (_upcomingEvents.value != null && _finishedEvents.value != null) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val upcomingResponse = ApiConfig.getApiService().getEvent("1")
                _upcomingEvents.postValue(upcomingResponse.listEvents?.filterNotNull() ?: emptyList())

                val finishedResponse = ApiConfig.getApiService().getEvent("0")
                _finishedEvents.postValue(finishedResponse.listEvents?.filterNotNull() ?: emptyList())
            } catch (e: Exception) {
                _errorMessage.postValue("Load Data Failed: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun checkLoadingStatus() {
        if (_upcomingEvents.value != null && _finishedEvents.value != null) {
            _isLoading.value = false
        }
    }
}
