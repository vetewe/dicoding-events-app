package com.dicoding.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.dicodingevent.data.response.*
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.*

class FinishedViewModel : ViewModel() {
    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    fun getFinishedEvents() {
        val client = ApiConfig.getApiService().getEvent("0")
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _finishedEvents.value =
                        response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FinishedViewModel"
    }
}