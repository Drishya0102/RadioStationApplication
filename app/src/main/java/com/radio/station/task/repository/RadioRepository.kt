package com.radio.station.task.repository

import com.google.gson.JsonParseException
import com.radio.station.task.api.RadioApiServices
import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.model.radioStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class RadioRepository {

    private val apiService = RetrofitInstance.apiService
    private val stationCache = mutableListOf<radioStation>()
    private val availabilityCache = mutableMapOf<String, String>()


    suspend fun getStations(): List<radioStation> {
        if (stationCache.isEmpty()) {
            stationCache.addAll(apiService.getEnglishStations())
        }
        return stationCache
    }

    suspend fun getStationAvailability(uuid: String): String {
        if (availabilityCache.containsKey(uuid)) {
            return availabilityCache[uuid] ?: "Unknown"
        }
        return try {
            val checks = apiService.getStationAvailability(uuid)
            val latestCheck = checks.maxByOrNull { it.timestamp }
            var status :String
            if (latestCheck != null) {
                status="Online" // Customize logic to infer "Online" or "Offline" based on other details
            } else {
                status="Offline"
            }

            availabilityCache[uuid] = status // Cache the result
            status
        }  catch (e: HttpException) {
            if (e.code() == 429) {
                "Rate Limit Exceeded - Please try again later"
            } else {
                "Error fetching availability: ${e.message()}"
            }
        } catch (e: Exception) {
            "Error fetching availability: ${e.message}"
        }
    }

    /* suspend fun getStations(): List<Pair<radioStation, String>>  {
         return withContext(Dispatchers.IO) {
             try {
                 val stations = apiService.getEnglishStations()
                 *//*stations.map { station ->
                    val availability = fetchLatestAvailability(station.stationuuid)
                    station to availability
                }*//*
                stations.map { station -> "Offline" }
            } catch (e: Exception) {
                emptyList() // Handle error gracefully
            }
        }
    }

    private suspend fun fetchLatestAvailability(stationUuid: String): String {
      *//*  return try {
            val checks = apiService.getStationAvailability(stationUuid)
            checks.maxByOrNull { it.timestamp }?.status ?: "Not Available"
        } catch (e: Exception) {
            "unknown" // Handle error
        }*//*
        return withContext(Dispatchers.IO) {
            try {
                // Fetch station availability from the API
                val checks = apiService.getStationAvailability(stationUuid)

                // Validate the response to avoid null or empty list issues
                if (checks.isNullOrEmpty()) {
                    return@withContext "No availability data found"
                }

                // Find the latest availability based on the timestamp
                val latestCheck = checks.maxByOrNull { it.timestamp ?: "0L" }

                // Return the status if available, or a fallback
                latestCheck?.status ?: "Status not available"
            } catch (e: HttpException) {
                // Handle HTTP-specific errors
                when (e.code()) {
                    429 -> "Too Many Requests - Please try again later"
                    404 -> "Station not found"
                    else -> "HTTP Error: ${e.message()}"
                }
            } catch (e: IOException) {
                // Handle network issues
                "Network error - Please check your connection"
            } catch (e: Exception) {
                // Handle any other exceptions
                "An unexpected error occurred: ${e.message}"
            }
        }
    }*/

}