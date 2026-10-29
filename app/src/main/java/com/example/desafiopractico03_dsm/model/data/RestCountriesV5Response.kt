package com.example.desafiopractico03_dsm.model.data

import com.google.gson.annotations.SerializedName

data class RestCountriesV5Response(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("data") val data: V5Data?
) {
    data class V5Data(
        @SerializedName("objects") val objects: List<Country>?
    )

    val countryList: List<Country>
        get() = data?.objects ?: emptyList()
}
