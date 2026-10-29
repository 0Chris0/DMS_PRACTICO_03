package com.example.desafiopractico03_dsm.model.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location") val location: Location?,
    @SerializedName("current") val current: Current?
) {
    data class Location(
        @SerializedName("name") val name: String?,
        @SerializedName("country") val country: String?,
        @SerializedName("localtime") val localtime: String?
    )

    data class Current(
        @SerializedName("temp_c") val tempC: Double?,
        @SerializedName("temp_f") val tempF: Double?,
        @SerializedName("condition") val condition: Condition?,
        @SerializedName("wind_kph") val windKph: Double?,
        @SerializedName("humidity") val humidity: Int?,
        @SerializedName("last_updated") val lastUpdated: String?
    ) {
        data class Condition(
            @SerializedName("text") val text: String?,
            @SerializedName("icon") val icon: String?
        )
    }

    val temperatureC: Double get() = current?.tempC ?: 0.0
    val conditionText: String get() = current?.condition?.text ?: "Desconocido"
    val conditionIcon: String get() = "https:" + (current?.condition?.icon ?: "")
    val windSpeedKph: Double get() = current?.windKph ?: 0.0
    val humidityValue: Int get() = current?.humidity ?: 0
    val updateTime: String get() = current?.lastUpdated ?: ""
}
