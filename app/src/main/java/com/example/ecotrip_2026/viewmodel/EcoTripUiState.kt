package com.example.ecotrip_2026.viewmodel

data class EcoTripUiState(
    val destino: String = "",
    val duracionTexto: String = "",
    val transporte: String = "",
    val viajeBajoCarbono: Boolean = false,
    val mensajeError: String? = null
)