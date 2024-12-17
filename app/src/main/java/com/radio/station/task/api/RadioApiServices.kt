package com.radio.station.task.api

import com.radio.station.task.model.radioStation
import com.radio.station.task.model.stationAvailabilty
import retrofit2.http.GET
import retrofit2.http.Path

interface RadioApiServices {

    @GET("json/stations/bylanguage/english")
    suspend fun getEnglishStations() : List<radioStation>

    @GET("json/checks/{stationUuid}")
    suspend fun getStationAvailability(@Path("stationUuid") stationUuid: String): List<stationAvailabilty>


}