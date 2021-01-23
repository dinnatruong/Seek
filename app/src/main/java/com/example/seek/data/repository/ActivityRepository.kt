package com.example.seek.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.seek.data.model.Activity
import com.example.seek.data.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ActivityRepository  {

    val activity = MutableLiveData<Activity>()

    fun getActivityDetails(type: String): MutableLiveData<Activity> {
        val call = RetrofitClient.boredApiInterface.getActivityByType(type)

        call.enqueue(object: Callback<Activity> {
            override fun onFailure(call: Call<Activity>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                if (!response.isSuccessful) return

                val data = response.body()
                data?.let {
                    activity.value = Activity(it.activity, it.type, it.participants, it.key)
                }
            }
        })

        return activity
    }
}