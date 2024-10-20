package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getEvent(
        @Query("active") active: String? = null,
        @Query("q") query: String? = null,
        @Query("id") id: String? = null,
        @Query("limit") limit: String? = null
    ): EventResponse
}
