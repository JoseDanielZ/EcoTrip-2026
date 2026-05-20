package com.tuempresa.ecotrip_2026.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tuempresa.ecotrip_2026.ui.screens.FormularioScreen
import com.tuempresa.ecotrip_2026.ui.screens.ResumenScreen

// Composable principal que define el grafo de navegación de la app
@Composable
fun EcoTripNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = FormularioRoute // Pantalla inicial
    ) {
        // Destino: pantalla del formulario
        composable<FormularioRoute> {
            FormularioScreen(navController = navController)
        }

        // Destino: pantalla de resumen (recibe parámetros serializados)
        composable<ResumenRoute> {
            ResumenScreen(navController = navController)
        }
    }
}