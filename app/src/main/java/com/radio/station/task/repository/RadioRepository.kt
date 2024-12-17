package com.radio.station.task.repository

import com.radio.station.task.api.RadioApiServices
import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.model.radioStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


class RadioRepository(private val apiService: RadioApiServices) {

    // Fetch stations list
    suspend fun getStations(): List<radioStation> {
        return withContext(Dispatchers.IO) {
            apiService.getEnglishStations()
        }
    }

    // Fetch availability status for a station
    suspend fun getAvailability(stationIds: List<String>): Map<String, String> {
        return coroutineScope {
            // Create a list of async tasks to fetch availability for each station
            val results = stationIds.map { stationId ->
                async {
                    // Fetch the availability for each station
                    val availability = apiService.getStationAvailability(stationId).firstOrNull()?.status ?: "offline"
                    stationId to availability
                }
            }

            // Wait for all the async tasks to complete and return the results as a map
            results.awaitAll().toMap()
        }
       /* val results = stationIds.map { stationId ->
            async {
                stationId to apiService.getStationAvailability(stationId).firstOrNull()?.status ?: "offline"
            }
        }*/

       // return results.awaitAll().toMap()  // Collect the results into a map
    }
}


   /* private val apiService = RetrofitInstance.apiService

    //new code
    private val stationCache = mutableMapOf<String, String>()

    suspend fun getStations(): List<Pair<radioStation, String>> = withContext(Dispatchers.IO) {
        val stations = apiService.getEnglishStations()

        stations.map { station ->
            val availability = stationCache[station.stationuuid] ?: fetchLatestAvailability(station.stationuuid).also {
                stationCache[station.stationuuid] = it
            }
            station to availability
        }
    }

    private suspend fun fetchLatestAvailability(uuid: String): String {
        return try {
            val checks = apiService.getStationAvailability(uuid)
            checks.maxByOrNull { it.timestamp }?.status ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }*/

  /*  // working code
    // Fetch all stations
    suspend fun getStations(): List<radioStation> {
        return try {
            apiService.getEnglishStations() // Fetch all stations
        } catch (e: Exception) {
            emptyList() // Return empty list in case of an error
        }
    }

    // Fetch the availability of a station using its UUID
    suspend fun getStationAvailability(stationUuid: String): String {
        return try {
            val availabilityList = apiService.getStationAvailability(stationUuid)
            // Get the most recent availability based on timestamp
            val latestAvailability = availabilityList.maxByOrNull { it.timestamp }
            latestAvailability?.status ?: "unknown" // Return status or "unknown" if not available
        } catch (e: Exception) {
            "unknown" // Return "unknown" if an error occurs
        }
    }

    */

    // old code
   /*

    suspend fun getStations(): List<Pair<radioStation, String>>  {
        return withContext(Dispatchers.IO) {
            try {
                val stations = apiService.getEnglishStations()
                stations.map { station ->
                    val availability = fetchLatestAvailability(station.stationuuid)
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
            "Unknown Error" // Handle error
            *//*val t=e.toString()
            return t*//*
        }
    }*/

//}