package com.radio.station.task.repository

import com.google.gson.JsonParseException
import com.radio.station.task.api.RadioApiServices
import com.radio.station.task.api.RetrofitInstance
import com.radio.station.task.model.radioStation
import com.radio.station.task.model.stationAvailabilty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class RadioRepository {

    private val apiService = RetrofitInstance.apiService
    suspend fun getStationsWithAvailability(): List<radioStation> = withContext(Dispatchers.IO) {
        val stations = apiService.getEnglishStations()

        // Fetch availability concurrently
        coroutineScope {
            val stationChecks = stations.chunked(20).map { chunk ->

                async {
                    chunk.map { station ->
                        val checks = getStationAvailability(station.stationuuid)
                        val status = checks.maxByOrNull { it.timestamp ?: "00" }
                            ?.apply { status = "Available" }?.status ?: "unAvailable"
                        station.copy(url = status)
                    }
                }
                // stationChecks.map { it.await() }
            }.awaitAll()
            stationChecks.flatten()
        }
    }

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

}