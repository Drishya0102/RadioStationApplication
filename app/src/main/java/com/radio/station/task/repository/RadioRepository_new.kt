package com.radio.station.task.repository

import android.util.Log
import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.model.radioStation
import com.radio.station.task.model.stationAvailabilty
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RadioRepository_new {

    private val apiService = RetrofitInstance.apiService

    // Fetch all English stations
    suspend fun getAllStations(): List<radioStation> {
        return apiService.getEnglishStations()
    }

    // Fetch station availability by station UUID
    suspend fun getStationAvailability1(stationUuid: String): List<stationAvailabilty> {
        return apiService.getStationAvailability(stationUuid)
    }

    // Combine station data and its availability status
    suspend fun getStationsWithAvailability1(): List<radioStation> {
        val stations = getAllStations()

        // Fetch availability for each station and add the most recent status
        return stations.map { station ->
            val availability = getStationAvailability(station.stationuuid)

            // Find the most recent availability by timestamp
            val latestStatus = availability
                .filter { it.timestamp.isNotEmpty() } // Ensure timestamp is not empty
                .maxByOrNull { it.timestamp ?: "00" }?.status
                ?: "Unknown" // Default value in case of empty or invalid data

            //.maxByOrNull { it.timestamp }?.status ?: "Unknown"

            // Return the station with the updated status
            station.copy(url = latestStatus)
        }
    }


    // Combine station data and availability status, handle errors gracefully
    suspend fun getStationsWithAvailability(): List<radioStation> {
        return try{
        val stations = getAllStations()

        stations.map { station ->
            try {
                // Get the station availability for the current station
                val availability = getStationAvailability(station.stationuuid)

                // Get the most recent availability status, fallback to "Unknown" in case of error
                if(availability.size==0)
                {
                    station.copy(url = "unAvailable")
                }
                else {
                    val latestStatus = getLatestAvailability(availability)
                    station.copy(url = latestStatus)
                }
                // Return the station with the updated status

            } catch (e: Exception) {
                // If any error occurs while fetching availability, assign "Unknown" status
                station.copy(url = "unAvailable")
            }

        }
        } catch (e: Exception) {
            // Catch any errors that occur while fetching the stations or availability
            Log.e("RadioRepository", "Error fetching stations: ${e.message}")
            emptyList() // Return an empty list if something fails
        } finally {
            // This block is executed regardless of whether an exception occurred or not
            Log.d("RadioRepository", "getStationsWithAvailability executed")
        }
    }

    // Fetch station availability by station UUID
    suspend fun getStationAvailability(stationUuid: String): List<stationAvailabilty> {
        return try {
            apiService.getStationAvailability(stationUuid)
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


    // Get the most recent availability, defaulting to "Unknown" in case of error
    private fun getLatestAvailability(availabilityList: List<stationAvailabilty>): String {
        //return availabilityList.maxByOrNull { it.timestamp ?: "00" }?.status="Online" ?: "Unknown"
        val status = availabilityList.maxByOrNull { it.timestamp ?: "00" }?.apply { status = "Available" }?.status ?: "unAvailable"
        return status
    }
}