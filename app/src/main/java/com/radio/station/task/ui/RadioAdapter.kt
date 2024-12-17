/*
package com.radio.station.task.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.databinding.CustomRadiostationsBinding
import com.radio.station.task.model.radioStation

class RadioAdapter(private val stations: List<radioStation>)://List<Pair<radioStation, String>>) :
    RecyclerView.Adapter<RadioAdapter.StationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val binding = CustomRadiostationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val (station, status) = stations[position]
        holder.bind(station, status)
    }

    override fun getItemCount(): Int = stations.size

    inner class StationViewHolder(private val binding: CustomRadiostationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(station: radioStation, status: String) {
            binding.stationName.text = station.name
            binding.stationCountry.text = station.country
            binding.stationStatus.text = station.url

            // Change color based on availability status
            binding.stationStatus.setTextColor(
                if (status == "online") {
                    Color.GREEN
                } else {
                    Color.RED
                }
            )
        }
    }
}
*/
