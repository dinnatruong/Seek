package com.example.seek.data.local

import androidx.lifecycle.LiveData
import com.example.seek.data.model.ActivityDao
import com.example.seek.data.model.ActivityEntity

class ActivityDBRepository(private val activityDao: ActivityDao) {

    suspend fun insertActivity(activityEntity: ActivityEntity) {
        activityDao.insert(activityEntity)
    }

    val allSavedActivities: LiveData<List<ActivityEntity>> = activityDao.getAllActivities()

}