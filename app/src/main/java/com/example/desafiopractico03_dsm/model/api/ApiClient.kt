package com.example.desafiopractico03_dsm.model.api

import com.example.desafiopractico03_dsm.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val tokenToUse = if (BuildConfig.REST_COUNTRIES_TOKEN.isNotBlank() && BuildConfig.REST_COUNTRIES_TOKEN != "dummy_token") {
        BuildConfig.REST_COUNTRIES_TOKEN
    } else {
        "rc_live_7949de7236824d8faccffc1cf1239a17"
    }

    private val restCountriesOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenToUse))
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val restCountriesRetrofit = Retrofit.Builder()
        .baseUrl("https://api.restcountries.com/")
        .client(restCountriesOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val restCountriesApi: RestCountriesApi = restCountriesRetrofit.create(RestCountriesApi::class.java)

    private val geoNamesOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val geoNamesRetrofit = Retrofit.Builder()
        .baseUrl("https://secure.geonames.org/")
        .client(geoNamesOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val geoNamesApi: GeoNamesApi = geoNamesRetrofit.create(GeoNamesApi::class.java)

    private val weatherOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .client(weatherOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApi: WeatherApi = weatherRetrofit.create(WeatherApi::class.java)
}
