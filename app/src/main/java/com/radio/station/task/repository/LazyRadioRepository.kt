package com.radio.station.task.repository

import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.model.radioStation
import com.radio.station.task.model.stationAvailabilty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LazyRadioRepository() {
    private val api = RetrofitInstance.apiService
    private var allStations: List<radioStation> = emptyList()
    private var lastIndex = 0 // Track the index of the last loaded station
    private val chunkSize = 20 // Number of stations to load at a time

    suspend fun fetchAllStations() {
        // Fetch the full station list once
        withContext(Dispatchers.IO) {
            if (allStations.isEmpty()) {
                allStations = api.getEnglishStations()
            }
        }
    }

    suspend fun getNextStations(): List<radioStation> = withContext(Dispatchers.IO) {
        // Return the next chunk of stations
        val nextStations = allStations.drop(lastIndex).take(chunkSize)
        lastIndex += nextStations.size

        // Fetch availability data for these stations
        nextStations.map { station ->
            val checks = getStationAvailability(station.stationuuid)
            val status = checks.maxByOrNull { it.timestamp ?: "00" }
                ?.apply { status = "Available" }?.status ?: "unAvailable"
            station.copy(url = status)
        }
    }


    suspend fun getStationAvailability(stationUuid: String): List<stationAvailabilty> {
        return try {
            api.getStationAvailability(stationUuid)
        } catch (e: HttpException) {
            if (e.code() == 429) {
                // Handle rate-limiting error (429) and return empty list
                emptyList()
            } else {
                emptyList() // Handle other HTTP exceptions
            }
        } catch (e: IOException) {
            emptyList() // Handle network errors
        } catch (e: Exception) {
            emptyList() // Handle any other exceptions
        }
    }

    fun hasMoreStations(): Boolean = lastIndex < allStations.size
}
