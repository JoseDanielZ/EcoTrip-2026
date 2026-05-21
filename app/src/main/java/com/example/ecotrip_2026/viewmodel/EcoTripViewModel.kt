package com.example.ecotrip_2026.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.ecotrip_2026.data.models.PreferenciaViaje
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EcoTripViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_DESTINO = "destino"
        private const val KEY_DURACION = "duracion"
        private const val KEY_TRANSPORTE = "transporte"
        private const val KEY_BAJO_CARBONO = "bajo_carbono"
    }

    private val _uiState = MutableStateFlow(
        EcoTripUiState(
            destino = savedStateHandle[KEY_DESTINO] ?: "",
            duracionTexto = savedStateHandle[KEY_DURACION] ?: "",
            transporte = savedStateHandle[KEY_TRANSPORTE] ?: "",
            viajeBajoCarbono = savedStateHandle[KEY_BAJO_CARBONO] ?: false
        )
    )

    val uiState: StateFlow<EcoTripUiState> = _uiState.asStateFlow()

    fun actualizarDestino(valor: String) {
        _uiState.update {
            it.copy(
                destino = valor,
                mensajeError = null
            )
        }
        savedStateHandle[KEY_DESTINO] = valor
    }

    fun actualizarDuracion(valor: String) {
        val textoLimpio = valor.filter { it.isDigit() }

        _uiState.update {
            it.copy(
                duracionTexto = textoLimpio,
                mensajeError = null
            )
        }

        savedStateHandle[KEY_DURACION] = textoLimpio
    }

    fun actualizarTransporte(valor: String) {
        _uiState.update {
            it.copy(
                transporte = valor,
                mensajeError = null
            )
        }

        savedStateHandle[KEY_TRANSPORTE] = valor
    }

    fun actualizarViajeBajoCarbono(valor: Boolean) {
        _uiState.update {
            it.copy(
                viajeBajoCarbono = valor,
                mensajeError = null
            )
        }

        savedStateHandle[KEY_BAJO_CARBONO] = valor
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _uiState.value
        val duracion = estadoActual.duracionTexto.toIntOrNull()

        val mensaje = when {
            estadoActual.destino.isBlank() -> "Ingrese un destino válido."
            duracion == null || duracion <= 0 -> "Ingrese una duración mayor a cero."
            estadoActual.transporte.isBlank() -> "Seleccione un tipo de transporte."
            else -> null
        }

        _uiState.update {
            it.copy(mensajeError = mensaje)
        }

        return mensaje == null
    }

    fun obtenerPreferenciaViaje(): PreferenciaViaje {
        val estadoActual = _uiState.value

        return PreferenciaViaje(
            destino = estadoActual.destino.trim(),
            duracionDias = estadoActual.duracionTexto.toIntOrNull() ?: 0,
            transporte = estadoActual.transporte,
            viajeBajoCarbono = estadoActual.viajeBajoCarbono
        )
    }

    fun limpiarFormulario() {
        _uiState.value = EcoTripUiState()

        savedStateHandle[KEY_DESTINO] = ""
        savedStateHandle[KEY_DURACION] = ""
        savedStateHandle[KEY_TRANSPORTE] = ""
        savedStateHandle[KEY_BAJO_CARBONO] = false
    }
}