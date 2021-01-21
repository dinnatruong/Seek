package com.example.seek.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seek.R
import com.example.seek.data.model.CategoryItem
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    data class CategoryClickUiEvent(val categoryItem: CategoryItem)

    private val clickSubject: PublishSubject<CategoryClickUiEvent> = PublishSubject.create()
    private lateinit var context: Context
    private var categories = mutableListOf<CategoryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getEscalateIncidentClickSubject(): Observable<CategoryClickUiEvent> {
        return clickSubject
    }

    fun setData(categories: List<CategoryItem>) {
        this.categories.clear()
        this.categories = categories.toMutableList()
    }

    fun setContext(context: Context) {
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val escalationLevel = categories[position]
        holder.bind(escalationLevel, context, clickSubject)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: CategoryItem, context: Context, clickSubject: PublishSubject<CategoryClickUiEvent>) {

            // Set category title
            itemView.categoryTitle.text = context.resources.getString(category.titleId)

            // Set category icon
            category.iconId?.let {
                itemView.categoryIcon.setImageResource(it)
            }

            // Set category background color
            category.backgroundId?.let {
                val backgroundColor = ContextCompat.getColor(context, it)
                itemView.categoryBackground.background.setTint(backgroundColor)
            }

            // Set category button click listener
            itemView.clicks()
                .map { CategoryClickUiEvent(category) }
                .subscribe(clickSubject)
        }
    }
}
