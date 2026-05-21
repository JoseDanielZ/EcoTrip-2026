package com.example.ecotrip_2026.data.models

data class PreferenciaViaje(
    val destino: String = "",
    val duracionDias: Int = 0,
    val transporte: String = "",
    val viajeBajoCarbono: Boolean = false
)