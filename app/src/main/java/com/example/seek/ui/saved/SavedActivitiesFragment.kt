package com.example.seek.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.seek.R
import com.example.seek.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedActivitiesFragment : BaseFragment() {

    private lateinit var savedActivitiesViewModel: SavedActivitiesViewModel
    private val savedActivityAdapter = SavedActivityAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        savedActivitiesViewModel =
                ViewModelProvider(this).get(SavedActivitiesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            savedActivitiesViewModel.getSavedActivityIds(it)?.observe(viewLifecycleOwner, Observer { savedActivities ->
                savedActivitiesViewModel.getSavedActivityDetails(savedActivities)
            })
        }

        savedActivitiesViewModel.savedActivities.observe(viewLifecycleOwner, Observer { savedActivities ->
            savedActivityAdapter.setData(savedActivities)
            savedActivityAdapter.notifyDataSetChanged()
        })


        with(savedActivitiesRecyclerView) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = savedActivityAdapter
        }

        subscribe(
            savedActivityAdapter.getSavedActivityClickSubject()
                .subscribe {
                    Toast.makeText(context, it.activity.activity, Toast.LENGTH_SHORT).show()
//                    val directions = HomeFragmentDirections.navigateToActivityDetails(it.categoryItem)
//
//                    with(findNavController()) {
//                        currentDestination?.getAction(directions.actionId)?.let {
//                            navigate(directions)
//                        }
//                    }
                }
        )
    }
}