package com.example.desafiopractico03_dsm.controller

import com.example.desafiopractico03_dsm.BuildConfig
import com.example.desafiopractico03_dsm.model.api.ApiClient
import com.example.desafiopractico03_dsm.model.data.GeoCity
import com.example.desafiopractico03_dsm.model.data.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

data class CityWeatherInfo(
    val city: GeoCity,
    val weather: WeatherResponse?,
    val error: String? = null,
    val isCapital: Boolean = false
)

class CountryDetailController {

    suspend fun getgetCitiesWithWeather(countryCode: String, capitalName: String, countryLat: Double = 0.0, countryLng: Double = 0.0): Result<List<CityWeatherInfo>> {
        return getCitiesWithWeather(countryCode, capitalName, countryLat, countryLng)
    }

    suspend fun getCitiesWithWeather(countryCode: String, capitalName: String, countryLat: Double = 0.0, countryLng: Double = 0.0): Result<List<CityWeatherInfo>> {
        return withContext(Dispatchers.IO) {
            try {
                val username = if (BuildConfig.GEONAMES_USERNAME.isNotBlank() && BuildConfig.GEONAMES_USERNAME != "demo") {
                    BuildConfig.GEONAMES_USERNAME
                } else {
                    "chris_dsm"
                }

                var cities = emptyList<GeoCity>()

                try {
                    val geoResponse = ApiClient.geoNamesApi.getCities(
                        countryCode = countryCode,
                        username = username
                    )
                    cities = geoResponse.geonames ?: emptyList()
                } catch (_: Exception) {}

                if (cities.isEmpty()) {
                    cities = listOf(
                        GeoCity(
                            name = if (capitalName.isNotBlank() && capitalName != "Sin capital") capitalName else "Capital",
                            population = 1000000L,
                            lat = if (countryLat != 0.0) countryLat else 13.6929,
                            lng = if (countryLng != 0.0) countryLng else -89.2182,
                            fcl = "P",
                            fcode = "PPLC",
                            countrycode = countryCode,
                            adminName1 = capitalName
                        )
                    )
                }

                val apiKey = if (BuildConfig.WEATHER_API_KEY.isNotBlank() && BuildConfig.WEATHER_API_KEY != "demo_key") {
                    BuildConfig.WEATHER_API_KEY
                } else {
                    "0e248d6823ba473a840150416262610"
                }

                val deferredWeather = cities.map { city ->
                    async {
                        val isCap = city.cityName.equals(capitalName, ignoreCase = true) || city.cityName.equals("Capital", ignoreCase = true)
                        val query = "${city.latitude},${city.longitude}"
                        try {
                            val weather = ApiClient.weatherApi.getCurrentWeather(apiKey, query)
                            CityWeatherInfo(city, weather, null, isCap)
                        } catch (e: Exception) {
                            CityWeatherInfo(city, null, e.localizedMessage ?: "Clima no disponible", isCap)
                        }
                    }
                }

                val results = deferredWeather.awaitAll()
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
