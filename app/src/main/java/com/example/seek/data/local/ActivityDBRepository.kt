package com.example.seek.data.local

import com.example.seek.data.model.ActivityDao
import com.example.seek.data.model.Activity
import io.reactivex.Observable

class ActivityDBRepository(private val activityDao: ActivityDao) {

    suspend fun insertActivity(activity: Activity) {
        activityDao.insert(activity)
    }

    suspend fun deleteActivity(key: String) {
        activityDao.delete(key)
    }

    fun getAllSavedActivities(): Observable<List<Activity>> = activityDao.getAllActivities()

    fun getActivityByKey(key: String): Observable<List<Activity>> = activityDao.getActivityByKey(key)

}