package com.example.desafiopractico03_dsm.model.api

import com.example.desafiopractico03_dsm.model.data.GeoNamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoNamesApi {
    @GET("searchJSON")
    suspend fun getCities(
        @Query("country") countryCode: String,
        @Query("featureClass") featureClass: String = "P",
        @Query("cities") cities: String = "cities15000",
        @Query("maxRows") maxRows: Int = 10,
        @Query("orderby") orderby: String = "population",
        @Query("username") username: String
    ): GeoNamesResponse
}
