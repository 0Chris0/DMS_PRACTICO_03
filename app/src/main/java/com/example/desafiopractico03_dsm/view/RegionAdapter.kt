package com.example.desafiopractico03_dsm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiopractico03_dsm.R

class RegionAdapter(
    private val regions: List<String>,
    private val onRegionClick: (String) -> Unit
) : RecyclerView.Adapter<RegionAdapter.RegionViewHolder>() {

    class RegionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textRegionName: TextView = itemView.findViewById(R.id.textRegionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_region, parent, false)
        return RegionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val region = regions[position]
        holder.textRegionName.text = region
        holder.itemView.setOnClickListener { onRegionClick(region) }
    }

    override fun getItemCount(): Int = regions.size
}
