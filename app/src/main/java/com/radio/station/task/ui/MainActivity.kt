package com.radio.station.task.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.R
import com.radio.station.task.repository.RadioRepository
import com.radio.station.task.viewmodel.RadioViewModel
import com.radio.station.task.viewmodel.RadioViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var radioViewModel: RadioViewModel
    //private lateinit var radioViewModel: RadioViewModel_new
    private lateinit var adapter: StationAdapter//RadioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.radioRecycle)
        adapter = StationAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Create ViewModel with RadioRepository
        val radioRepository = RadioRepository()
        val viewModelFactory = RadioViewModelFactory(radioRepository)
        radioViewModel = ViewModelProvider(this, viewModelFactory).get(RadioViewModel::class.java)

        // Observe the stationsLiveData and update the RecyclerView when data changes
        radioViewModel.stations.observe(this, Observer { stations ->
            adapter.submitList(stations)
        })

        radioViewModel.loading.observe(this, Observer { isLoading ->
            findViewById<ProgressBar>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Load stations and their availability
        radioViewModel.loadStations()


    }
}