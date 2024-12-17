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

    private lateinit var viewModel: RadioViewModel
    private lateinit var adapter: StationAdapter//RadioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = RadioRepository()
        viewModel = ViewModelProvider(this, RadioViewModelFactory(repository)).get(RadioViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.radioRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StationAdapter()
        recyclerView.adapter = adapter

        viewModel.stations.observe(this, Observer { stations ->
            //adapter=RadioAdapter(stations)
            adapter.submitList(stations)
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            findViewById<ProgressBar>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.loadStations()
    }
}