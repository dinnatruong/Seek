package com.example.seek.ui.activitydetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek.data.model.Activity
import com.example.seek.data.model.ActivityEntity
import com.example.seek.data.local.ActivityDBRepository
import com.example.seek.data.local.ActivityDatabase
import com.example.seek.data.remote.ActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.shuffle

class ActivityDetailsViewModel : ViewModel() {

    var activityDetails: MutableLiveData<Activity>? = null

    fun getActivityDetails(type: String): LiveData<Activity>? {
        var activityType = type.toLowerCase()

        if (activityType == "random") {
            val categories = listOf(
                "education",
                "social",
                "diy",
                "music",
                "relaxation",
                "charity",
                "cooking",
                "busywork",
                "recreational"
            )
            shuffle(categories)
            activityType = categories.first()
        }

        activityDetails = ActivityRepository.getActivityDetailsByType(activityType)
        return activityDetails
    }

    fun saveActivity(context: Context) {
        val activityEntity = activityDetails?.value?.key?.let { ActivityEntity(activityId = it) }

        val activityDao = ActivityDatabase.getDatabase(context).activityDao()
        val activityDBRepository =
            ActivityDBRepository(activityDao)

        activityEntity?.let {
            viewModelScope.launch(Dispatchers.IO) {
                activityDBRepository.insertActivity(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        activityDetails?.value = null
    }
}