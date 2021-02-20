package com.example.seek.ui.activitydetails

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek.data.model.Activity
import com.example.seek.data.local.ActivityDBRepository
import com.example.seek.data.local.ActivityDatabase
import com.example.seek.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.shuffle

class ActivityDetailsViewModel : ViewModel() {

    private var _activityDetails = MutableLiveData<Activity>().apply {
        value = null
    }

    val activityDetails: LiveData<Activity>? = _activityDetails

    private var _errorMessage = MutableLiveData<String>().apply {
        value = null
    }

    val errorMessage: LiveData<String>? = _errorMessage

    private var _isSaved = MutableLiveData<Boolean>().apply {
        value = false
    }

    val isSaved: LiveData<Boolean>? = _isSaved

    private var subscription: Disposable? = null

    fun getActivityDetails(type: String?, key: String?) {
        type?.let { getActivityDetailsByCategory(it.toLowerCase()) }
        key?.let { getActivityDetailsByKey(it) }
    }

    private fun getActivityDetailsByCategory(activityType: String) {
        val type = if (activityType == "random") getRandomCategory() else activityType

        subscription =
            RetrofitClient.boredApiInterface.getActivityByType(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onGetActivityDetailsSuccess(result) },
                    { onGetActivityDetailsError() })
    }

    private fun getRandomCategory(): String {
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
        return categories.first()
    }

    private fun getActivityDetailsByKey(key: String) {
        subscription =
            RetrofitClient.boredApiInterface.getActivityByKey(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onGetActivityDetailsSuccess(result) },
                    { onGetActivityDetailsError() })
    }

    private fun onGetActivityDetailsSuccess(result: Activity) {
        _activityDetails.value = result
    }

    private fun onGetActivityDetailsError() {
        _errorMessage.value = "Failed to fetch activity details."
    }

    fun checkSavedState(context: Context, key: String) {
        val activityDao = ActivityDatabase.getDatabase(context).activityDao()
        val db = ActivityDBRepository(activityDao)

        subscription =
            db.getActivityByKey(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onGetSavedActivitySuccess(result) },
                    { onGetActivityDetailsError() })
    }

    private fun onGetSavedActivitySuccess(result: List<Activity>) {
        _isSaved.value = result.isNotEmpty()
    }

    fun handleSaveButtonClick(context: Context) {
        val activityEntity = activityDetails?.value?.key?.let { Activity(key = it) }
        val activityDao = ActivityDatabase.getDatabase(context).activityDao()
        val activityDBRepository = ActivityDBRepository(activityDao)

        activityEntity?.let {
            viewModelScope.launch(Dispatchers.IO) {

                if (_isSaved.value == false) {
                    // Add activity to saved list
                    activityDBRepository.insertActivity(it)

                } else {
                    // Remove activity from saved list
                    it.key?.let { key -> activityDBRepository.deleteActivity(key) }

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _activityDetails.value = null
        _errorMessage.value = null
        subscription?.dispose()
    }
}