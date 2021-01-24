package com.example.seek.ui.saved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seek.R
import com.example.seek.data.model.Activity
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_saved_activity.view.*

class SavedActivityAdapter : RecyclerView.Adapter<SavedActivityAdapter.ViewHolder>() {

    data class SavedActivityClickUiEvent(val activity: Activity)

    private val clickSubject: PublishSubject<SavedActivityClickUiEvent> = PublishSubject.create()
    private var savedActivities = mutableListOf<Activity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_activity, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedActivities.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getSavedActivityClickSubject(): Observable<SavedActivityClickUiEvent> {
        return clickSubject
    }

    fun setData(savedActivities: List<Activity>) {
        this.savedActivities.clear()
        this.savedActivities = savedActivities.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val savedActivity = savedActivities[position]
        holder.bind(savedActivity, clickSubject)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(savedActivity: Activity, clickSubject: PublishSubject<SavedActivityClickUiEvent>) {

            // Set activity description
            itemView.savedActivityDescription.text = savedActivity.activity

            // Set saved activity click listener
            itemView.clicks()
                .map { SavedActivityClickUiEvent(savedActivity) }
                .subscribe(clickSubject)
        }
    }
}