package com.example.desafiopractico03_dsm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiopractico03_dsm.R
import com.example.desafiopractico03_dsm.controller.CountryListController
import kotlinx.coroutines.launch

class CountriesActivity : AppCompatActivity() {

    private lateinit var countryListController: CountryListController
    private lateinit var progressBar: ProgressBar
    private lateinit var textEmpty: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        val region = intent.getStringExtra("EXTRA_REGION") ?: "Americas"

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Países de $region"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        countryListController = CountryListController()
        progressBar = findViewById(R.id.progressBar)
        textEmpty = findViewById(R.id.textEmpty)
        recyclerView = findViewById(R.id.recyclerViewCountries)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCountries(region)
    }

    private fun loadCountries(region: String) {
        progressBar.visibility = View.VISIBLE
        textEmpty.visibility = View.GONE
        recyclerView.visibility = View.GONE

        lifecycleScope.launch {
            val result = countryListController.getCountriesByRegion(region)
            progressBar.visibility = View.GONE
            if (result.isSuccess) {
                val countries = result.getOrNull() ?: emptyList()
                if (countries.isEmpty()) {
                    textEmpty.visibility = View.VISIBLE
                    textEmpty.text = "No se encontraron países en esta región."
                } else {
                    recyclerView.visibility = View.VISIBLE
                    val adapter = CountryAdapter(countries) { country ->
                        val intent = Intent(this@CountriesActivity, CountryDetailActivity::class.java).apply {
                            putExtra("EXTRA_COUNTRY_JSON", com.google.gson.Gson().toJson(country))
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                }
            } else {
                textEmpty.visibility = View.VISIBLE
                textEmpty.text = "Error al cargar países: ${result.exceptionOrNull()?.localizedMessage ?: "Verifique su conexión"}"
            }
        }
    }
}
