package com.tuempresa.ecotrip_2026.data.models

// Modelo de datos que representa las preferencias de un viaje
// Persona 3 usará esta clase para persistir en DataStore
data class PreferenciaViaje(
    val destino: String = "",        // Nombre del destino
    val duracion: Int = 0,           // Duración en días
    val huellaCarbono: String = ""   // Huella de carbono estimada
)