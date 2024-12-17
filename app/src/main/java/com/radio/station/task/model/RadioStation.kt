package com.radio.station.task.model

//I have used Camelcase naming Convention here for better understanding

data class radioStation(val name:String,val stationuuid:String,val country:String,val url:String)

data class stationAvailabilty(val status:String,val timestamp: String)
