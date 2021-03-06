package com.example.seek.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

        // Fetch and display list of saved activities
        context?.let {
            savedActivitiesViewModel.getSavedActivities(it)
            savedActivityAdapter.setContext(it)
        }

        savedActivitiesViewModel.savedActivities.observe(viewLifecycleOwner, Observer { savedActivities ->
            savedActivityAdapter.setData(savedActivities)
            savedActivityAdapter.notifyDataSetChanged()
        })

        // Set up RecyclerView for saved activities
        with(savedActivitiesRecyclerView) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = savedActivityAdapter
        }

        // Set click listener for saved activities
        subscribe(
            savedActivityAdapter.getSavedActivityClickSubject()
                .subscribe {

                    // Navigate to activity details
                    val directions = SavedActivitiesFragmentDirections.navigateToActivityDetails(null, it.activity.key)

                    with(findNavController()) {
                        currentDestination?.getAction(directions.actionId)?.let {
                            navigate(directions)
                        }
                    }
                }
        )

        // Display any error messages
        savedActivitiesViewModel.errorMessage
            ?.observe(viewLifecycleOwner, Observer { errorMessage ->
                errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            })
    }
}