package com.example.seek.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.seek.R
import com.example.seek.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val categoryAdapter = CategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(categoriesRecyclerView) {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
            adapter = categoryAdapter
        }

        context?.let { categoryAdapter.setContext(it) }

        homeViewModel.getCategories().observe(viewLifecycleOwner, Observer {
            categoryAdapter.setData(it)
        })

        subscribe(
            categoryAdapter.getEscalateIncidentClickSubject()
                .subscribe {
                    val title = getString(it.categoryItem.titleId)
                    val directions = HomeFragmentDirections.navigateToActivityDetails().setCategoryTitle(title)

                    with(findNavController()) {
                        currentDestination?.getAction(directions.actionId)?.let {
                            navigate(directions)
                        }
                    }
                }
        )
    }
}