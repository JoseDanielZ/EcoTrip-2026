package com.example.ecotrip_2026.viewmodel

// Estado de la UI: única fuente de verdad que la pantalla observa reactivamente
data class EcoTripUiState(
    val nombreViajero: String = "",        // Campo de texto — persiste en DataStore
    val destino: String = "",              // Campo de texto — persiste en SavedStateHandle
    val duracionTexto: String = "",        // Campo numérico — persiste en SavedStateHandle
    val transporte: String = "",           // RadioButton seleccionado
    val viajeBajoCarbono: Boolean = false, // Switch — persiste en DataStore
    val esViajeGrupal: Boolean = false,    // Switch — persiste en SavedStateHandle
    val mensajeError: String? = null       // Mensaje de validación, null si no hay error
)