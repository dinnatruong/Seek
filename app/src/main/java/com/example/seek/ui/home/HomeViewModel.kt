package com.example.seek.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seek.data.model.Category
import com.example.seek.data.model.CategoryItem

class HomeViewModel : ViewModel() {

    private var categories = MutableLiveData<List<CategoryItem>>()

    init {
        categories.value = Category.itemMap.values.toList()
    }

    fun getCategories(): LiveData<List<CategoryItem>> {
        return categories
    }


}