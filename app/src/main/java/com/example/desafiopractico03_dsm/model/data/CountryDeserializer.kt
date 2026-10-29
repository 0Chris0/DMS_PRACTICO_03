package com.example.desafiopractico03_dsm.model.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class CountryDeserializer : JsonDeserializer<Country> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Country {
        return try {
            val obj = json?.asJsonObject ?: JsonObject()

            var nameVal = "País"
            if (obj.has("names") && obj.get("names").isJsonObject) {
                val namesObj = obj.getAsJsonObject("names")
                if (namesObj.has("common") && namesObj.get("common").isJsonPrimitive) {
                    nameVal = namesObj.get("common").asString
                }
            }

            var capitalVal = "Sin capital"
            if (obj.has("capitals") && obj.get("capitals").isJsonArray) {
                val capsArr = obj.getAsJsonArray("capitals")
                if (capsArr.size() > 0 && capsArr.get(0).isJsonObject) {
                    val capObj = capsArr.get(0).asJsonObject
                    if (capObj.has("name") && capObj.get("name").isJsonPrimitive) {
                        capitalVal = capObj.get("name").asString
                    }
                }
            }

            val regionVal = if (obj.has("region") && obj.get("region").isJsonPrimitive) obj.get("region").asString else "Americas"
            val subregionVal = if (obj.has("subregion") && obj.get("subregion").isJsonPrimitive) obj.get("subregion").asString else ""

            var popVal = 0L
            if (obj.has("population") && obj.get("population").isJsonPrimitive) {
                try { popVal = obj.get("population").asLong } catch (_: Exception) {}
            }

            var flagVal = ""
            if (obj.has("flag") && obj.get("flag").isJsonObject) {
                val flagObj = obj.getAsJsonObject("flag")
                if (flagObj.has("url_png") && flagObj.get("url_png").isJsonPrimitive) {
                    flagVal = flagObj.get("url_png").asString
                } else if (flagObj.has("url_svg") && flagObj.get("url_svg").isJsonPrimitive) {
                    flagVal = flagObj.get("url_svg").asString
                }
            }

            var cca2Val = "US"
            if (obj.has("codes") && obj.get("codes").isJsonObject) {
                val codesObj = obj.getAsJsonObject("codes")
                if (codesObj.has("alpha_2") && codesObj.get("alpha_2").isJsonPrimitive) {
                    cca2Val = codesObj.get("alpha_2").asString
                }
            }

            val currenciesList = mutableListOf<Map<String, Any>>()
            if (obj.has("currencies") && obj.get("currencies").isJsonArray) {
                for (curEl in obj.getAsJsonArray("currencies")) {
                    if (curEl.isJsonObject) {
                        val curObj = curEl.asJsonObject
                        val map = mutableMapOf<String, Any>()
                        if (curObj.has("code")) map["code"] = curObj.get("code").asString
                        if (curObj.has("name")) map["name"] = curObj.get("name").asString
                        if (curObj.has("symbol")) map["symbol"] = curObj.get("symbol").asString
                        currenciesList.add(map)
                    }
                }
            }

            val languagesList = mutableListOf<Map<String, Any>>()
            if (obj.has("languages") && obj.get("languages").isJsonArray) {
                for (langEl in obj.getAsJsonArray("languages")) {
                    if (langEl.isJsonObject) {
                        val langObj = langEl.asJsonObject
                        val map = mutableMapOf<String, Any>()
                        if (langObj.has("name")) map["name"] = langObj.get("name").asString
                        if (langObj.has("bcp47")) map["bcp47"] = langObj.get("bcp47").asString
                        languagesList.add(map)
                    }
                }
            }

            val latlngArr = mutableListOf<Double>()
            if (obj.has("coordinates") && obj.get("coordinates").isJsonObject) {
                val coordObj = obj.getAsJsonObject("coordinates")
                try {
                    if (coordObj.has("lat")) latlngArr.add(coordObj.get("lat").asDouble)
                    if (coordObj.has("lng")) latlngArr.add(coordObj.get("lng").asDouble)
                } catch (_: Exception) {}
            }

            Country(
                name = Country.Name(nameVal, nameVal),
                capitalAny = listOf(capitalVal),
                region = regionVal,
                subregion = subregionVal,
                population = popVal,
                flagsAny = flagVal,
                cca2Any = cca2Val,
                cca3 = cca2Val,
                currenciesAny = currenciesList,
                languagesAny = languagesList,
                latlngAny = latlngArr
            )
        } catch (e: Exception) {
            Country(
                name = Country.Name("País", "País"),
                capitalAny = listOf("Sin capital"),
                region = "Americas",
                subregion = "",
                population = 0L,
                flagsAny = "",
                cca2Any = "US",
                cca3 = "US",
                currenciesAny = emptyList<Any>(),
                languagesAny = emptyList<Any>(),
                latlngAny = emptyList<Any>()
            )
        }
    }
}
