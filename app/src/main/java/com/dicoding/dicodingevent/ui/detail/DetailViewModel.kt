package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _eventDetail = MutableLiveData<ListEventsItem?>()
    val eventDetail: LiveData<ListEventsItem?> = _eventDetail

    fun getEventDetail(eventId: Int) {
        val client = ApiConfig.getApiService().getEvent(id = eventId.toString())
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _eventDetail.value =
                        response.body()?.listEvents?.firstOrNull { it?.id == eventId }
                } else {
                    _eventDetail.value = null
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _eventDetail.value = null
            }
        })
    }
}
