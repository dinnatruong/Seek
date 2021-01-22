package com.example.seek.ui.activitydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.seek.R
import com.example.seek.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_activity_details.*

class ActivityDetailsFragment : BaseFragment() {

    private var activityTitle = ""
    private val args by navArgs<ActivityDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_activity_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTitle = args.categoryTitle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityDescription.text = activityTitle
    }
}