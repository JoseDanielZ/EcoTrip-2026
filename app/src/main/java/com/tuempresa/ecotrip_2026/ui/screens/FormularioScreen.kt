package com.tuempresa.ecotrip_2026.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

// Pantalla del formulario — stub temporal hasta que Persona 4 implemente la UI
@Composable
fun FormularioScreen(navController: NavHostController) {
    // Contenedor centrado que ocupa toda la pantalla
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("FormularioScreen - placeholder")
    }
}