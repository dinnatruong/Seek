package com.example.seek.data.local

import androidx.lifecycle.LiveData
import com.example.seek.data.model.ActivityDao
import com.example.seek.data.model.Activity

class ActivityDBRepository(private val activityDao: ActivityDao) {

    suspend fun insertActivity(activity: Activity) {
        activityDao.insert(activity)
    }

    val allSavedActivities: LiveData<List<Activity>> = activityDao.getAllActivities()

}