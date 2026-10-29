# Desafío Práctico #03 - Desarrollo de Software para Móviles (DSM)

Aplicación Android desarrollada en Kotlin que consume tres APIs públicas: **REST Countries**, **GeoNames** y **WeatherAPI**, siguiendo la arquitectura **MVC** (Model-View-Controller).

## 🚀 Características Principales

1. **Pantalla Inicial (Regiones)**: Listado de regiones mundiales (Africa, Americas, Asia, Europe, Oceania).
2. **Pantalla de Países**: Listado de países por región mostrando nombre, bandera y capital.
3. **Pantalla de Detalle de País**:
   - Especificaciones completas (nombre, oficial, capital, región, subregión, población, monedas, idiomas).
   - Listado de hasta 10 ciudades principales (con la capital destacada) obtenidos de **GeoNames**.
   - Clima actual de cada ciudad obtenido de **WeatherAPI** (con manejo de errores individual por ciudad para no bloquear la pantalla).
4. **Mapa Interactivo**: Integrado con **osmdroid**, mostrando marcadores para cada ciudad con su nombre y clima actual. Al hacer clic en una ciudad de la lista, el mapa se centra en ella.
5. **Arquitectura MVC**: Separación estricta entre Modelos, Vistas y Controladores.
6. **Manejo de Credenciales**: Las claves de API se configuran a través de `local.properties` (ver `local.properties.example`).

---

## 🔑 Configuración de Credenciales (`local.properties`)

Cree o edite el archivo `local.properties` en la raíz del proyecto agregando sus credenciales:

```properties
REST_COUNTRIES_TOKEN=su_token_bearer
GEONAMES_USERNAME=su_usuario_geonames
WEATHER_API_KEY=su_api_key_de_weatherapi
```

---

## 📦 Enlace de Descarga del APK

* Enlace de descarga del APK: [Descargar APK (Próximamente / Enlace en Aula Digital)](#)
