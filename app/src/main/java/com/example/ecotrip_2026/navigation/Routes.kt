package com.example.ecotrip_2026.navigation

import kotlinx.serialization.Serializable

// Ruta de la pantalla del formulario (sin parámetros)
@Serializable
object FormularioRoute

// Ruta type-safe de la pantalla de resumen
// El compilador verifica en tiempo de compilación que todos los parámetros sean provistos
@Serializable
data class ResumenRoute(
    val destino: String,           // Destino del viaje
    val duracion: Int,             // Duración en días
    val huellaCarbono: String,     // "Baja" o "Estándar"
    val esViajeGrupal: Boolean     // Si viaja en grupo o individual
)