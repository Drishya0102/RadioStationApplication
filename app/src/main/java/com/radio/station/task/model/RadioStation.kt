package com.radio.station.task.model

import com.google.gson.annotations.SerializedName

//I have used Camelcase naming Convention here for better understanding

data class radioStation(
    @SerializedName("stationuuid") val stationuuid: String,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("language") val language: String,
    val status:String)
    //val name:String,val stationUuid:String,val checkuuid:String,val country:String,val url:String)

data class stationAvailabilty(val status:String,val timestamp: String)

data class StationWithStatus(
    val name: String,
    val status: String,
    val lang: String
)