package com.radio.station.task.repository

import com.radio.station.task.api.RadioApiServices
import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.model.radioStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


class RadioRepository {

    private val apiService = RetrofitInstance.apiService

    suspend fun getStations(): List<Pair<radioStation, String>>  {
        return withContext(Dispatchers.IO) {
            try {
                val stations = apiService.getEnglishStations()
                stations.map { station ->
                    val availability = fetchLatestAvailability(station.uuid)
                    station to availability
                }
            } catch (e: Exception) {
                emptyList() // Handle error gracefully
            }
        }
    }

    private suspend fun fetchLatestAvailability(stationUuid: String): String {
        return try {
            val checks = apiService.getStationAvailability(stationUuid)
            checks.maxByOrNull { it.timestamp }?.status ?: "Not Available"
        } catch (e: Exception) {
            "unknown" // Handle error
        }
    }

}