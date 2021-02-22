package com.example.seek.ui.saved

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seek.data.local.ActivityDBRepository
import com.example.seek.data.local.ActivityDatabase
import com.example.seek.data.model.Activity
import com.example.seek.data.model.Category
import com.example.seek.data.model.CategoryItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SavedActivitiesViewModel : ViewModel() {

    private val _savedActivities = MutableLiveData<List<Activity>>().apply {
        value = emptyList()
    }

    val savedActivities: LiveData<List<Activity>> = _savedActivities

    private var _errorMessage = MutableLiveData<String>().apply {
        value = null
    }

    val errorMessage: LiveData<String>? = _errorMessage

    private var subscription: Disposable? = null

    fun getSavedActivities(context: Context) {
        val activityDao = ActivityDatabase.getDatabase(context).activityDao()
        val db = ActivityDBRepository(activityDao)

        subscription =
            db.getAllSavedActivities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onGetSavedActivitiesSuccess(result, context) },
                    { onGetSavedActivitiesError() })
    }

    private fun onGetSavedActivitiesSuccess(result: List<Activity>, context: Context){
        _savedActivities.value = result
        setActivityCategories(context)
    }

    private fun onGetSavedActivitiesError(){
        _errorMessage.value = "Failed to fetch saved activities."
    }

    private fun setActivityCategories(context: Context) {
        savedActivities.value?.let { savedActivities ->

            for (activity in savedActivities) {
                val category = activity.type?.let { it -> getCategory(it, context) }
                activity.category = category
            }
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


    override fun onCleared() {
        super.onCleared()
        _errorMessage.value = null
        subscription?.dispose()
    }
}