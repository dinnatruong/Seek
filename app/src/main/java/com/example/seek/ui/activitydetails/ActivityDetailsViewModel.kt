package com.example.seek.ui.activitydetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek.data.model.Activity
import com.example.seek.data.local.ActivityDBRepository
import com.example.seek.data.local.ActivityDatabase
import com.example.seek.data.model.Category
import com.example.seek.data.model.CategoryItem
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

    val activityDetails: LiveData<Activity> = _activityDetails

    private var _errorMessage = MutableLiveData<String>().apply {
        value = null
    }

    val errorMessage: LiveData<String>? = _errorMessage

    private var _isSaved = MutableLiveData<Boolean>().apply {
        value = false
    }

    val isSaved: LiveData<Boolean>? = _isSaved

    private var _backgroundResId = MutableLiveData<Int>().apply {
        value = null
    }

    val backgroundResId: LiveData<Int>? = _backgroundResId

    private var subscription: Disposable? = null

    fun getActivityDetails(type: String?, key: String?, context: Context) {
        type?.let { getActivityDetailsByCategory(it.toLowerCase(), context) }
        key?.let { getActivityDetailsByKey(it, context) }
    }

    private fun getActivityDetailsByCategory(activityType: String, context: Context) {
        val type = if (activityType == "random") getRandomCategory() else activityType

        subscription =
            RetrofitClient.boredApiInterface.getActivityByType(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onGetActivityDetailsSuccess(result, context) },
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

    private fun getActivityDetailsByKey(key: String, context: Context) {
        subscription =
            RetrofitClient.boredApiInterface.getActivityByKey(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onGetActivityDetailsSuccess(result, context) },
                    { onGetActivityDetailsError() })
    }

    private fun onGetActivityDetailsSuccess(result: Activity, context: Context) {
        _activityDetails.value = result
        setBackgroundColour(context)
    }

    private fun onGetActivityDetailsError() {
        _errorMessage.value = "Failed to fetch activity details."
    }

    private fun setBackgroundColour(context: Context) {
        activityDetails.value?.let {
            val category = activityDetails.value?.type?.let { type -> getCategory(type, context) }
            _backgroundResId.value = category?.backgroundId
        }
    }

    private fun getCategory(type: String, context: Context): CategoryItem? {
        val categories = Category.itemMap.values.toList()

        for (category in categories) {
            if (context.getString(category.titleId).toLowerCase() == type) {
                return category
            }
        }
        return null
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
        val activityDao = ActivityDatabase.getDatabase(context).activityDao()
        val activityDBRepository = ActivityDBRepository(activityDao)

        activityDetails.value?.let {
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