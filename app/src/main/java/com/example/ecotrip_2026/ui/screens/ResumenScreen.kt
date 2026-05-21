package com.example.ecotrip_2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecotrip_2026.navigation.FormularioRoute
import com.example.ecotrip_2026.navigation.ResumenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    navController: NavHostController,
    resumenRoute: ResumenRoute
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del viaje") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // popUpTo limpia el back stack para evitar instancias acumuladas
                            // launchSingleTop evita duplicar FormularioScreen
                            navController.navigate(FormularioRoute) {
                                popUpTo(FormularioRoute) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    // Usa MaterialTheme — cambia automáticamente con Material You
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Viaje confirmado",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Ruta configurada correctamente",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Plantillas de cadena idiomáticas de Kotlin (string templates)
                    Text(
                        text = "Destino: ${resumenRoute.destino}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Duración: ${resumenRoute.duracion} días",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Huella de carbono: ${resumenRoute.huellaCarbono}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // Muestra el booleano de viaje grupal como texto legible
                    Text(
                        text = "Modalidad: ${if (resumenRoute.esViajeGrupal) "Viaje en grupo" else "Viaje individual"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}