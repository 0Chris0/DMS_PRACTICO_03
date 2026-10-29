package com.example.desafiopractico03_dsm.model.api

import com.example.desafiopractico03_dsm.model.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("lang") lang: String = "es"
    ): WeatherResponse
}
