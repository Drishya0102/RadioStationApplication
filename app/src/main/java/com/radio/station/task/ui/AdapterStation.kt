package com.radio.station.task.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radio.station.task.R
import com.radio.station.task.model.radioStation

sealed class ListItem {
    data class StationItem(val station: radioStation) : ListItem()
    object LoadingItem : ListItem()
}

class AdapterStation : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ListItem>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.StationItem -> 0
            is ListItem.LoadingItem -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_radiostations, parent, false)
            StationViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.StationItem -> (holder as StationViewHolder).bind(item.station)
            is ListItem.LoadingItem -> Unit

        }

    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<radioStation>) {
        if(newItems.size!=0) {
            items.clear()
            items.addAll(newItems.map { ListItem.StationItem(it) })
            notifyDataSetChanged()
        }
    }

    fun addLoading() {
        if (items.lastOrNull() !is ListItem.LoadingItem) {
            items.add(ListItem.LoadingItem)
            notifyItemInserted(items.size - 1)
        }
    }

    fun removeLoading() {
        if (items.lastOrNull() is ListItem.LoadingItem) {
            val position = items.size - 1
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.stationName)
        private val statusTextView: TextView = itemView.findViewById(R.id.stationStatus)
        private val countryTextView: TextView = itemView.findViewById(R.id.stationCountry)

        fun bind(station: radioStation) {
            nameTextView.text = station.name
            statusTextView.text = station.url
            countryTextView.text=station.country
            statusTextView.setTextColor(
                if (station.url == "Available") {
                    Color.GREEN
                } else {
                    Color.RED
                })
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
