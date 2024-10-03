package com.dicoding.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.dicodingevent.data.response.*
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.*

class UpcomingViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUpcomingEvents(query: String? = null) {
        if (_isLoading.value == true) return // Cegah pemanggilan berulang
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent("1", query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "UpcomingViewModel"
    }
}
