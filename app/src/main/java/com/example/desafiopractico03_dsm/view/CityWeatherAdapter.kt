package com.example.desafiopractico03_dsm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.desafiopractico03_dsm.R
import com.example.desafiopractico03_dsm.controller.CityWeatherInfo

class CityWeatherAdapter(
    private val items: List<CityWeatherInfo>,
    private val onCityClick: (CityWeatherInfo) -> Unit
) : RecyclerView.Adapter<CityWeatherAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCityName: TextView = itemView.findViewById(R.id.textCityName)
        val textCapitalBadge: TextView = itemView.findViewById(R.id.textCapitalBadge)
        val imageWeatherIcon: ImageView = itemView.findViewById(R.id.imageWeatherIcon)
        val textTemperature: TextView = itemView.findViewById(R.id.textTemperature)
        val textCondition: TextView = itemView.findViewById(R.id.textCondition)
        val textWind: TextView = itemView.findViewById(R.id.textWind)
        val textHumidity: TextView = itemView.findViewById(R.id.textHumidity)
        val textWeatherError: TextView = itemView.findViewById(R.id.textWeatherError)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city_weather, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item = items[position]
        holder.textCityName.text = item.city.cityName

        if (item.isCapital) {
            holder.textCapitalBadge.visibility = View.VISIBLE
        } else {
            holder.textCapitalBadge.visibility = View.GONE
        }

        if (item.error != null && item.weather == null) {
            holder.textWeatherError.visibility = View.VISIBLE
            holder.textWeatherError.text = item.error
            holder.textTemperature.text = "-- °C"
            holder.textCondition.text = "-"
            holder.textWind.text = "Viento: -- kph"
            holder.textHumidity.text = "Humedad: --%"
            holder.imageWeatherIcon.visibility = View.GONE
        } else if (item.weather != null) {
            holder.textWeatherError.visibility = View.GONE
            val weather = item.weather
            holder.textTemperature.text = "${weather.temperatureC} °C"
            holder.textCondition.text = weather.conditionText
            holder.textWind.text = "Viento: ${weather.windSpeedKph} kph"
            holder.textHumidity.text = "Humedad: ${weather.humidityValue}%"

            val iconUrl = weather.conditionIcon
            if (iconUrl.isNotBlank()) {
                holder.imageWeatherIcon.visibility = View.VISIBLE
                holder.imageWeatherIcon.load(iconUrl)
            } else {
                holder.imageWeatherIcon.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener { onCityClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
