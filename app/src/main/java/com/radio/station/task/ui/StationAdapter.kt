package com.radio.station.task.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.databinding.CustomRadiostationsBinding
import com.radio.station.task.model.radioStation

class StationAdapter : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    private var stations: List<radioStation> = emptyList()

    // ViewHolder to hold views for each item
    inner class StationViewHolder(private val binding: CustomRadiostationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(station: radioStation) {
                binding.stationName.text = station.name
                binding.stationCountry.text = station.country
                binding.stationStatus.text = station.url

                // Change color based on availability status
                binding.stationStatus.setTextColor(
                    if (station.url == "Available") {
                        Color.GREEN
                    } else {
                        Color.RED
                    }
                )
            }
    }

    // Set the new list of stations to display
    fun submitList(newStations: List<radioStation>) {
        stations = newStations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val binding = CustomRadiostationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stations[position])
    }

    override fun getItemCount(): Int = stations.size
}