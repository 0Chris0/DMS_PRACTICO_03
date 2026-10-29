package com.example.desafiopractico03_dsm.model.api

import com.example.desafiopractico03_dsm.model.data.RestCountriesV5Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestCountriesApi {
    @GET("countries/v5")
    suspend fun getAllCountries(): RestCountriesV5Response

    @GET("countries/v5")
    suspend fun getCountriesByRegion(@Query("region") region: String): RestCountriesV5Response

    @GET("countries/v5/codes.alpha_2/{code}")
    suspend fun getCountryByCode(@Path("code") code: String): RestCountriesV5Response
}
