package com.example.desafiopractico03_dsm.controller

import com.example.desafiopractico03_dsm.model.api.ApiClient
import com.example.desafiopractico03_dsm.model.data.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryListController {
    suspend fun getCountriesByRegion(region: String): Result<List<Country>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiClient.restCountriesApi.getCountriesByRegion(region)
                var list = response.countryList

                if (list.isEmpty()) {
                    val allResponse = ApiClient.restCountriesApi.getAllCountries()
                    val allList = allResponse.countryList
                    list = allList.filter { 
                        it.region.equals(region, ignoreCase = true) ||
                        it.subregion?.contains(region, ignoreCase = true) == true
                    }
                    if (list.isEmpty()) {
                        list = allList
                    }
                } else {
                    val filtered = list.filter { 
                        it.region.equals(region, ignoreCase = true) ||
                        it.subregion?.contains(region, ignoreCase = true) == true
                    }
                    if (filtered.isNotEmpty()) {
                        list = filtered
                    }
                }

                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
