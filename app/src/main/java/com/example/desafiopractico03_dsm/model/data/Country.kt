package com.example.desafiopractico03_dsm.model.data

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name", alternate = ["names"]) val name: Name?,
    @SerializedName("capital", alternate = ["capitals"]) val capitalAny: Any?,
    @SerializedName("region") val region: String?,
    @SerializedName("subregion") val subregion: String?,
    @SerializedName("population") val population: Long?,
    @SerializedName("flags", alternate = ["flag"]) val flagsAny: Any?,
    @SerializedName("cca2", alternate = ["codes"]) val cca2Any: Any?,
    @SerializedName("cca3") val cca3: String?,
    @SerializedName("currencies") val currenciesAny: Any?,
    @SerializedName("languages") val languagesAny: Any?,
    @SerializedName("latlng", alternate = ["coordinates"]) val latlngAny: Any?
) {
    data class Name(
        @SerializedName("common") val common: String?,
        @SerializedName("official") val official: String?
    )

    data class Flags(
        @SerializedName("png", alternate = ["url_png"]) val png: String?,
        @SerializedName("svg", alternate = ["url_svg"]) val svg: String?,
        @SerializedName("alt") val alt: String?
    )

    data class Currency(
        @SerializedName("name") val name: String?,
        @SerializedName("symbol") val symbol: String?
    )

    val commonName: String
        get() = name?.common ?: "País"

    val officialName: String
        get() = name?.official ?: commonName

    val capitalName: String
        get() = when (capitalAny) {
            is String -> capitalAny
            is List<*> -> {
                val first = capitalAny.firstOrNull()
                if (first is Map<*, *>) first["name"]?.toString() ?: "Sin capital"
                else first?.toString() ?: "Sin capital"
            }
            is Map<*, *> -> capitalAny["name"]?.toString() ?: "Sin capital"
            else -> "Sin capital"
        }

    val flagUrl: String
        get() = when (flagsAny) {
            is String -> flagsAny
            is Map<*, *> -> (flagsAny["png"] ?: flagsAny["svg"] ?: flagsAny["url_png"] ?: flagsAny["url_svg"])?.toString() ?: ""
            else -> ""
        }

    val cca2: String?
        get() = when (cca2Any) {
            is String -> cca2Any
            is Map<*, *> -> cca2Any["alpha_2"]?.toString() ?: cca2Any["code"]?.toString()
            else -> "US"
        }

    val latlng: List<Double>?
        get() = when (latlngAny) {
            is List<*> -> latlngAny.mapNotNull { it?.toString()?.toDoubleOrNull() }
            is Map<*, *> -> {
                val lat = latlngAny["lat"]?.toString()?.toDoubleOrNull()
                val lng = latlngAny["lng"]?.toString()?.toDoubleOrNull()
                if (lat != null && lng != null) listOf(lat, lng) else null
            }
            else -> null
        }

    val currencies: Map<String, Currency>?
        get() = try {
            when (currenciesAny) {
                is Map<*, *> -> {
                    val map = mutableMapOf<String, Currency>()
                    for ((k, v) in currenciesAny) {
                        if (k is String && v is Map<*, *>) {
                            map[k] = Currency(v["name"]?.toString(), v["symbol"]?.toString())
                        }
                    }
                    map
                }
                is List<*> -> {
                    val map = mutableMapOf<String, Currency>()
                    for (item in currenciesAny) {
                        if (item is Map<*, *>) {
                            val code = item["code"]?.toString() ?: "CUR"
                            map[code] = Currency(item["name"]?.toString(), item["symbol"]?.toString())
                        }
                    }
                    map
                }
                else -> null
            }
        } catch (_: Exception) {
            null
        }

    val languages: Map<String, String>?
        get() = try {
            when (languagesAny) {
                is Map<*, *> -> {
                    val map = mutableMapOf<String, String>()
                    for ((k, v) in languagesAny) {
                        if (k is String && v != null) map[k] = v.toString()
                    }
                    map
                }
                is List<*> -> {
                    val map = mutableMapOf<String, String>()
                    for (item in languagesAny) {
                        if (item is Map<*, *>) {
                            val name = item["name"]?.toString() ?: item["bcp47"]?.toString() ?: "Lang"
                            map[name] = name
                        } else if (item is String) {
                            map[item] = item
                        }
                    }
                    map
                }
                else -> null
            }
        } catch (_: Exception) {
            null
        }
}
