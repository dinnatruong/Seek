package com.example.seek.ui.activitydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.seek.R
import com.example.seek.ui.base.BaseFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.seek.data.model.CategoryItem
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.fragment_activity_details.*

class ActivityDetailsFragment : BaseFragment() {

    private var categoryItem: CategoryItem? = null
    private var activityKey: String? = null
    private val args by navArgs<ActivityDetailsFragmentArgs>()
    private lateinit var activityDetailsViewModel: ActivityDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_activity_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.categoryItem?.let {
            categoryItem = it
        }

        args.activityKey?.let {
            activityKey = it
        }

        activityDetailsViewModel = ViewModelProvider(this).get(ActivityDetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityType = categoryItem?.titleId?.let { getString(it) }

        // Fetch and display activity description
        activityDetailsViewModel.getActivityDetails(activityType, activityKey)

        activityDetailsViewModel.activityDetails
            ?.observe(viewLifecycleOwner, Observer { activityDetails ->
                activityDescription.text = activityDetails?.activity
            })

        // Display any error messages
        activityDetailsViewModel.errorMessage
            ?.observe(viewLifecycleOwner, Observer { errorMessage ->
                errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            })

        // Set background colour based on activity category
        context?.let {
            descriptionBackground.background.setTint(
                ContextCompat.getColor(
                    it,
                    categoryItem?.backgroundId ?: R.color.slate
                )
            )
        }

        // Set click listener for home button
        subscribe(
            homeButton.clicks().subscribe {
                findNavController().popBackStack()
            }
        )

        // Set click listener for save button
        subscribe(
            saveButton.clicks().subscribe {
                context?.let { activityDetailsViewModel.saveActivity(it) }
            }
        )
    }
}