package com.example.seek.ui.activitydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seek.data.model.Activity
import com.example.seek.data.repository.ActivityRepository
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

        activityDetails = ActivityRepository.getActivityDetails(activityType)
        return activityDetails
    }

    override fun onCleared() {
        super.onCleared()
        activityDetails?.value = null
    }
}