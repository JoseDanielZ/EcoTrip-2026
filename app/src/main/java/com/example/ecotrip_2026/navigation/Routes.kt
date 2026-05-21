// Navegación type-safe para EcoTrip 2026
package com.example.ecotrip_2026.navigation

import kotlinx.serialization.Serializable

// Ruta de la pantalla del formulario (sin parámetros)
@Serializable
object FormularioRoute

// Ruta de la pantalla de resumen (con parámetros type-safe)
@Serializable
data class ResumenRoute(
    val destino: String,       // Destino del viaje
    val duracion: Int,         // Duración en días
    val huellaCarbono: String  // Huella de carbono calculada
)