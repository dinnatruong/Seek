package com.example.seek.data.repository

import com.example.seek.data.model.Activity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BoredApiInterface {

    @GET("api/activity")
    fun getActivityByType(@Query("type") type: String) : Call<Activity>

}