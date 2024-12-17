package com.radio.station.task.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.databinding.CustomRadiostationsBinding
import com.radio.station.task.model.radioStation

/*class RadioAdapter(private var stations: List<Pair<radioStation, String>>) :
    RecyclerView.Adapter<RadioAdapter.StationViewHolder>() {*/
class RadioAdapter : RecyclerView.Adapter<RadioAdapter.StationViewHolder>() {

    private val stations = mutableListOf<Pair<radioStation, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val binding = CustomRadiostationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }

    fun submitList(data: List<Pair<radioStation, String>>) {
        stations.clear()
        stations.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val (station, status) = stations[position]
        holder.bind(station, status)
        /*val station = stations[position]
        holder.bind(station)*/
    }

    override fun getItemCount(): Int = stations.size


    /*fun setStations(newStations: List<Pair<radioStation, String>>) {
        val diffCallback = StationDiffCallback(stations, newStations)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        stations = newStations
        diffResult.dispatchUpdatesTo(this)
    }
*/
    inner class StationViewHolder(private val binding: CustomRadiostationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(station: radioStation, status: String) {
            binding.stationName.text = station.name
            binding.stationCountry.text = station.country
            binding.stationStatus.text = status

            // Change color based on availability status
            binding.stationStatus.setTextColor(
                if (status == "online") {
                    Color.GREEN
                } else {
                    Color.RED
                }
            )
        }

      fun bind1(station: Pair<radioStation, String>) {
          binding.stationName.text = station.first.name
          binding.stationCountry.text = station.first.country
          binding.stationStatus.text = station.second
          // Change color based on availability status
          binding.stationStatus.setTextColor(
              if (station.second == "online") {
                  Color.GREEN
              } else {
                  Color.RED
              }
          )
      }
    }

    class StationDiffCallback(
        private val oldList: List<Pair<radioStation, String>>,
        private val newList: List<Pair<radioStation, String>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].first.stationuuid == newList[newItemPosition].first.stationuuid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}


