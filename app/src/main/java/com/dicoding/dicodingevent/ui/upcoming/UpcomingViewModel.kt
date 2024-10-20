package com.dicoding.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class UpcomingViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getUpcomingEvents(query: String? = null) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvent("1", query)
                _upcomingEvents.postValue(response.listEvents?.filterNotNull() ?: emptyList())
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Load Data Failed: ${e.message}")
                _isLoading.postValue(false)
            }
        }
    }
}
