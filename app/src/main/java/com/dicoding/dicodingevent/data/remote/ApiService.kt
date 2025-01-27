package com.dicoding.dicodingevent.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.dicoding.dicodingevent.data.response.DetailResponse
import com.dicoding.dicodingevent.data.response.EventResponse

interface ApiService {
    @GET("events")
    fun getListEventsItem(
        @Query("active") page: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<DetailResponse>
}
