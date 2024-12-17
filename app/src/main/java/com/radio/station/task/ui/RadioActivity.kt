package com.radio.station.task.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.R
import com.radio.station.task.viewmodel.LazyRadioViewModel
import com.radio.station.task.viewmodel.LazyRadioViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class RadioActivity  : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var stationAdapter: AdapterStation
    private val viewModel: LazyRadioViewModel by viewModels {
        LazyRadioViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.radioRecycle)
        stationAdapter = AdapterStation()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = stationAdapter

        // Observe station data
        lifecycleScope.launchWhenStarted {
            viewModel.stations.collectLatest { stationList ->
                stationAdapter.setItems(stationList)
                recyclerView.post {
                    // Ensure the RecyclerView gets updated immediately
                    stationAdapter.notifyDataSetChanged()
                }
            }
        }

        // Observe loading state to show/hide footer
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collectLatest { isLoading ->
                if (isLoading) {
                    stationAdapter.addLoading()
                } else {
                    stationAdapter.removeLoading()
                    recyclerView.post {
                        // Ensure the RecyclerView gets updated immediately
                        stationAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        // Add lazy loading on scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Trigger loading when near the end
                if (lastVisibleItemPosition + 5 >= totalItemCount) {
                    viewModel.loadMoreStations()
                }
            }
        })
    }
}