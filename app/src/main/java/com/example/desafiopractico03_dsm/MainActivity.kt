package com.example.desafiopractico03_dsm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiopractico03_dsm.controller.RegionController
import com.example.desafiopractico03_dsm.view.CountriesActivity
import com.example.desafiopractico03_dsm.view.RegionAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var regionController: RegionController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regions)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Regiones del Mundo (Desafío 3)"

        regionController = RegionController()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRegions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val regions = regionController.getRegions()
        val adapter = RegionAdapter(regions) { region ->
            val intent = Intent(this, CountriesActivity::class.java).apply {
                putExtra("EXTRA_REGION", region)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
