/*
package com.radio.station.task.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.databinding.CustomRadiostationsBinding
import com.radio.station.task.model.StationWithStatus
import com.radio.station.task.viewmodel.RadioViewModel

// Create a ViewHolder class for the adapter
class RadioStationAdapter : RecyclerView.Adapter<RadioStationAdapter.RadioStationViewHolder>() {

    private var stations = listOf<StationWithStatus>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioStationViewHolder {
        val binding =
            CustomRadiostationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RadioStationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RadioStationViewHolder, position: Int) {
        val station = stations[position]
        holder.bind(station)
    }

    override fun getItemCount(): Int = stations.size

    // Method to update the list of stations and notify the adapter of the change
    fun updateStations(newStations: List<StationWithStatus>) {
        stations = newStations
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    // ViewHolder to bind the item views
    inner class RadioStationViewHolder(private val binding: CustomRadiostationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(station: StationWithStatus) {
            binding.stationName.text = station.name
            binding.stationStatus.text = station.status
            binding.stationCountry.text = station.lang
        }
    }
}*/
