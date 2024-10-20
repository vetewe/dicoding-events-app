package com.dicoding.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class FinishedViewModel : ViewModel() {
    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getFinishedEvents(query: String? = null) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvent("0", query)
                _finishedEvents.postValue(response.listEvents?.filterNotNull() ?: emptyList())
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Load Data Failed: ${e.message}")
                _isLoading.postValue(false)
            }
        }
    }
}
