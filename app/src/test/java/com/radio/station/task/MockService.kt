package com.radio.station.task

import com.radio.station.task.api.RadioApiServices
import com.radio.station.task.model.radioStation
import com.radio.station.task.model.stationAvailabilty


class MockApiService : RadioApiServices {


    override suspend fun getEnglishStations(): List<radioStation> {
        return listOf(
            radioStation( "Station 1", "uuid1","USA", "English"),
            radioStation("Station 2", "uuid2","Canada", "English"),
            radioStation( "Station 3","uuid3", "UK", "English")
        )
    }

    override suspend fun getStationAvailability(uuid: String): List<stationAvailabilty> {
        // Return mock availability data based on the station's UUID
        return when (uuid) {
            "uuid1" -> listOf(
                stationAvailabilty("Available", "2024-12-14 12:42:29"),
                stationAvailabilty("unAvailable", "2024-12-14 12:42:29")
            )
            "uuid2" -> listOf(
                stationAvailabilty("unAvailable", "2024-12-14 12:42:29")
            )
            "uuid3" -> listOf(
                stationAvailabilty("Available", "2024-12-14 12:42:29"),
                stationAvailabilty("Available", "2024-12-14 12:42:29")
            )
            else -> emptyList()
        }
    }
}
