package com.example.desafiopractico03_dsm.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.desafiopractico03_dsm.R
import com.example.desafiopractico03_dsm.controller.CityWeatherInfo
import com.example.desafiopractico03_dsm.controller.CountryDetailController
import com.example.desafiopractico03_dsm.model.data.Country
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

class CountryDetailActivity : AppCompatActivity() {

    private lateinit var countryDetailController: CountryDetailController
    private lateinit var mapView: MapView
    private lateinit var progressBarCities: ProgressBar
    private lateinit var textCitiesError: TextView
    private lateinit var recyclerViewCities: RecyclerView

    private var country: Country? = null
    private val cityMarkers = mutableMapOf<String, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val configuration = Configuration.getInstance()
        configuration.load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        configuration.userAgentValue = "DesafioPractico03_DSM/1.0 (Educational Android App; contact@udb.edu.sv)"
        configuration.osmdroidBasePath = File(cacheDir, "osmdroid")
        configuration.osmdroidTileCache = File(cacheDir, "osmdroid/tiles")

        setContentView(R.layout.activity_country_detail)

        val countryJson = intent.getStringExtra("EXTRA_COUNTRY_JSON")
        country = Gson().fromJson(countryJson, Country::class.java)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = country?.commonName ?: "Detalle del País"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        countryDetailController = CountryDetailController()

        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.WIKIMEDIA)
        mapView.setUseDataConnection(true)
        mapView.setMultiTouchControls(true)

        progressBarCities = findViewById(R.id.progressBarCities)
        textCitiesError = findViewById(R.id.textCitiesError)
        recyclerViewCities = findViewById(R.id.recyclerViewCities)
        recyclerViewCities.layoutManager = LinearLayoutManager(this)

        displayCountryDetails()
        loadCitiesAndWeather()
    }

    private fun displayCountryDetails() {
        val c = country ?: return

        findViewById<ImageView>(R.id.imageDetailFlag).load(c.flagUrl) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_report_image)
        }

        findViewById<TextView>(R.id.textDetailName).text = c.commonName
        findViewById<TextView>(R.id.textDetailOfficialName).text = c.officialName
        findViewById<TextView>(R.id.textDetailCapital).text = "Capital: ${c.capitalName}"
        findViewById<TextView>(R.id.textDetailRegion).text = "Región: ${c.region ?: "-"}"
        findViewById<TextView>(R.id.textDetailSubregion).text = "Subregión: ${c.subregion ?: "-"}"
        findViewById<TextView>(R.id.textDetailPopulation).text = "Población: %,d".format(c.population ?: 0)

        val currenciesStr = c.currencies?.entries?.joinToString(", ") { "${it.value.name} (${it.value.symbol})" } ?: "-"
        findViewById<TextView>(R.id.textDetailCurrencies).text = "Monedas: $currenciesStr"

        val languagesStr = c.languages?.values?.joinToString(", ") ?: "-"
        findViewById<TextView>(R.id.textDetailLanguages).text = "Idiomas: $languagesStr"

        val latlng = c.latlng
        if (latlng != null && latlng.size >= 2) {
            val startPoint = GeoPoint(latlng[0], latlng[1])
            mapView.controller.setZoom(6.0)
            mapView.controller.setCenter(startPoint)
        } else {
            mapView.controller.setZoom(4.0)
            mapView.controller.setCenter(GeoPoint(0.0, 0.0))
        }
    }

    private fun loadCitiesAndWeather() {
        val c = country ?: return
        val countryCode = c.cca2 ?: c.cca3 ?: "US"
        val capitalName = c.capitalName
        val latlng = c.latlng
        val countryLat = latlng?.getOrNull(0) ?: 0.0
        val countryLng = latlng?.getOrNull(1) ?: 0.0

        progressBarCities.visibility = View.VISIBLE
        textCitiesError.visibility = View.GONE
        recyclerViewCities.visibility = View.GONE

        lifecycleScope.launch {
            val result = countryDetailController.getCitiesWithWeather(countryCode, capitalName, countryLat, countryLng)
            progressBarCities.visibility = View.GONE

            if (result.isSuccess) {
                val cityInfos = result.getOrNull() ?: emptyList()
                if (cityInfos.isEmpty()) {
                    textCitiesError.visibility = View.VISIBLE
                    textCitiesError.text = "No se encontraron ciudades principales para este país."
                } else {
                    recyclerViewCities.visibility = View.VISIBLE
                    setupMapMarkers(cityInfos)

                    val adapter = CityWeatherAdapter(cityInfos) { info ->
                        centerMapOnCity(info)
                    }
                    recyclerViewCities.adapter = adapter
                }
            } else {
                textCitiesError.visibility = View.VISIBLE
                textCitiesError.text = result.exceptionOrNull()?.localizedMessage ?: "Error al cargar ciudades"
            }
        }
    }

    private fun setupMapMarkers(cityInfos: List<CityWeatherInfo>) {
        mapView.overlays.clear()
        cityMarkers.clear()

        var firstPoint: GeoPoint? = null

        for (info in cityInfos) {
            val point = GeoPoint(info.city.latitude, info.city.longitude)
            if (firstPoint == null) firstPoint = point

            val marker = Marker(mapView)
            marker.position = point
            marker.title = info.city.cityName
            val tempStr = if (info.weather != null) "${info.weather.temperatureC} °C - ${info.weather.conditionText}" else "Clima no disponible"
            marker.snippet = tempStr
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            mapView.overlays.add(marker)
            cityMarkers[info.city.cityName] = marker
        }

        if (firstPoint != null) {
            mapView.controller.setZoom(7.0)
            mapView.controller.setCenter(firstPoint)
        }
        mapView.invalidate()
    }

    private fun centerMapOnCity(info: CityWeatherInfo) {
        val point = GeoPoint(info.city.latitude, info.city.longitude)
        mapView.controller.setZoom(10.0)
        mapView.controller.setCenter(point)

        val marker = cityMarkers[info.city.cityName]
        marker?.showInfoWindow()
        Toast.makeText(this, "Centrado en ${info.city.cityName}", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
