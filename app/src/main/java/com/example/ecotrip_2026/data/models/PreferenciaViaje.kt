package com.example.ecotrip_2026.data.models

// Modelo de datos inmutable que representa las preferencias fijas del usuario
data class PreferenciaViaje(
    val nombreViajero: String = "",      // Nombre del viajero (persiste en DataStore)
    val destino: String = "",            // Destino del viaje (persiste en SavedStateHandle)
    val duracionDias: Int = 0,           // Duración en días (persiste en SavedStateHandle)
    val transporte: String = "",         // Tipo de transporte ecológico seleccionado
    val viajeBajoCarbono: Boolean = false, // Switch de huella de carbono (persiste en DataStore)
    val esViajeGrupal: Boolean = false   // Si el viaje es en grupo (requerido por la ruta type-safe)
)