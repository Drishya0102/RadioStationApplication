package com.radio.station.task.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.R
import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.repository.RadioRepository
import com.radio.station.task.viewmodel.RadioViewModel
import com.radio.station.task.viewmodel.RadioViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var radioRepository: RadioRepository
    private lateinit var radioViewModel: RadioViewModel
    private lateinit var adapter: RadioStationAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize your repository
        radioRepository = RadioRepository(RetrofitInstance.apiService) // Adjust this according to how you initialize your repository

        // Initialize ViewModel using ViewModelFactory
        val viewModelFactory = RadioViewModelFactory(radioRepository)
        radioViewModel = ViewModelProvider(this, viewModelFactory).get(RadioViewModel::class.java)

         val recyclerView = findViewById<RecyclerView>(R.id.radioRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressBar)

        // Set up adapter
        adapter = RadioStationAdapter()//emptyList())
        recyclerView.adapter = adapter

        // Observe loading state to show/hide the ProgressBar
        lifecycleScope.launch {
            radioViewModel.isLoading.collect { isLoading ->
                showLoading(isLoading) // Show or hide progress bar based on isLoading value
            }
        }

        // Observe stations and update the adapter
        lifecycleScope.launch {
            radioViewModel.stations.collect { stations ->
                // Log the data to ensure it's coming in correctly
                Log.d("MainActivity", "Stations: $stations")

                // Update the adapter with the stations
                adapter.updateStations(stations)
            }
        }

        // Load the stations from ViewModel
        radioViewModel.loadStations()


        /* viewModel.stations.observe(this, Observer { stations ->
             adapter = RadioAdapter(stations)
             recyclerView.adapter = adapter
         })

         viewModel.loading.observe(this, Observer { isLoading ->
             findViewById<ProgressBar>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
         })

         viewModel.loadStations()*/
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}