package com.example.ecotrip_2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecotrip_2026.data.datastore.DataStoreManager
import com.example.ecotrip_2026.navigation.ResumenRoute
import com.example.ecotrip_2026.viewmodel.EcoTripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    navController: NavHostController,
    viewModel: EcoTripViewModel = viewModel(
        factory = EcoTripViewModel.factory(
            dataStoreManager = DataStoreManager(LocalContext.current)
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val opcionesTransporte = listOf("Tren", "Bicicleta", "Vehículo Eléctrico")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("EcoTrip 2026") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        val preferencia = viewModel.obtenerPreferenciaViaje()
                        val huella = if (preferencia.viajeBajoCarbono) "Baja" else "Estándar"

                        // Navega con parámetros type-safe; el compilador valida los tipos
                        navController.navigate(
                            ResumenRoute(
                                destino = preferencia.destino,
                                duracion = preferencia.duracionDias,
                                huellaCarbono = huella,
                                esViajeGrupal = preferencia.esViajeGrupal
                            )
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Continuar"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    // Color dinámico: responde automáticamente al Material You del sistema
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Configuración del viaje",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo nombre del viajero — persiste en DataStore (disco)
                    TextField(
                        value = uiState.nombreViajero,
                        onValueChange = { viewModel.actualizarNombreViajero(it) },
                        label = { Text("Nombre del viajero") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo destino — persiste en SavedStateHandle (Process Death)
                    TextField(
                        value = uiState.destino,
                        onValueChange = { viewModel.actualizarDestino(it) },
                        label = { Text("Destino") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo duración — solo números, persiste en SavedStateHandle
                    TextField(
                        value = uiState.duracionTexto,
                        onValueChange = { viewModel.actualizarDuracion(it) },
                        label = { Text("Duración en días") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Transporte ecológico",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // RadioButtons — selección de tipo de transporte
                    opcionesTransporte.forEach { opcion ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.transporte == opcion,
                                onClick = { viewModel.actualizarTransporte(opcion) }
                            )
                            Text(
                                text = opcion,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch huella de carbono — persiste en DataStore (preferencia global)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Ruta de baja huella de carbono")
                        Switch(
                            checked = uiState.viajeBajoCarbono,
                            onCheckedChange = { viewModel.actualizarViajeBajoCarbono(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Switch viaje grupal — persiste en SavedStateHandle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Viaje en grupo")
                        Switch(
                            checked = uiState.esViajeGrupal,
                            onCheckedChange = { viewModel.actualizarViajeGrupal(it) }
                        )
                    }

                    // Error de validación — solo visible si hay mensaje
                    uiState.mensajeError?.let { mensaje ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}