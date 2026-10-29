package com.example.desafiopractico03_dsm.model.data

import com.google.gson.annotations.SerializedName

data class GeoNamesResponse(
    @SerializedName("geonames") val geonames: List<GeoCity>?
)

data class GeoCity(
    @SerializedName("name") val name: String?,
    @SerializedName("population") val population: Long?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
    @SerializedName("fcl") val fcl: String?,
    @SerializedName("fcode") val fcode: String?,
    @SerializedName("countrycode") val countrycode: String?,
    @SerializedName("adminName1") val adminName1: String?
) {
    val cityName: String get() = name ?: "Ciudad"
    val latitude: Double get() = lat ?: 0.0
    val longitude: Double get() = lng ?: 0.0
}
