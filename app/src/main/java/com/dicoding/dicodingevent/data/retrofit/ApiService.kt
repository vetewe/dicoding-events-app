package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") active: String
    ): Call<EventResponse>
}