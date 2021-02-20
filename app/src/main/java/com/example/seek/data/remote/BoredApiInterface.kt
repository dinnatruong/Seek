package com.example.seek.data.remote

import com.example.seek.data.model.Activity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BoredApiInterface {

    @GET("api/activity")
    fun getActivityByType(@Query("type") type: String) : Observable<Activity>

    @GET("api/activity")
    fun getActivityByKey(@Query("key") key: String) : Observable<Activity>

}