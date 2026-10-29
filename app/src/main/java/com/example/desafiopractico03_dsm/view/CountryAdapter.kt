package com.example.desafiopractico03_dsm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.desafiopractico03_dsm.R
import com.example.desafiopractico03_dsm.model.data.Country

class CountryAdapter(
    private val countries: List<Country>,
    private val onCountryClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageFlag: ImageView = itemView.findViewById(R.id.imageFlag)
        val textCountryName: TextView = itemView.findViewById(R.id.textCountryName)
        val textCapital: TextView = itemView.findViewById(R.id.textCapital)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.textCountryName.text = country.commonName
        holder.textCapital.text = "Capital: ${country.capitalName}"

        val flagUrl = country.flagUrl
        if (flagUrl.isNotBlank()) {
            holder.imageFlag.load(flagUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_report_image)
            }
        } else {
            holder.imageFlag.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener { onCountryClick(country) }
    }

    override fun getItemCount(): Int = countries.size
}
