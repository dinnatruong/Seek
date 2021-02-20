package com.example.seek.ui.saved

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seek.data.local.ActivityDBRepository
import com.example.seek.data.local.ActivityDatabase
import com.example.seek.data.model.Activity
import com.example.seek.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SavedActivitiesViewModel : ViewModel() {

    private val _savedActivities = MutableLiveData<List<Activity>>().apply {
        value = emptyList()
    }
    val savedActivities: LiveData<List<Activity>> = _savedActivities

    private var subscription: Disposable? = null

    fun getSavedActivityIds(context: Context): LiveData<List<Activity>>? {
        val activityDao = ActivityDatabase.getDatabase(context).activityDao()
        val db = ActivityDBRepository(activityDao)
        return db.allSavedActivities
    }

    fun getSavedActivityDetails(savedActivities: List<Activity>) {
        _savedActivities.value = emptyList()
        for (savedActivity in savedActivities) {
            if (savedActivity.key !=  null) {
                subscription =
                    RetrofitClient.boredApiInterface.getActivityByKey(savedActivity.key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { result -> onGetSavedActivityDetailsSuccess(result) },
                            { onGetSavedActivityDetailsError() })
            }
        }
    }

    private fun onGetSavedActivityDetailsSuccess(result: Activity){
        val newList = _savedActivities.value?.toMutableList()
        newList?.add(result)
        _savedActivities.value = newList
    }

    private fun onGetSavedActivityDetailsError(){
        TODO("Not yet implemented")
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}