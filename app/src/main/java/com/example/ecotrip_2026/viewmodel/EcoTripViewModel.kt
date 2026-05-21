package com.example.ecotrip_2026.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ecotrip_2026.data.datastore.DataStoreManager
import com.example.ecotrip_2026.data.models.PreferenciaViaje
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EcoTripViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    companion object {
        // Claves para SavedStateHandle (Process Death: campos temporales del formulario)
        private const val KEY_DESTINO = "destino"
        private const val KEY_DURACION = "duracion"
        private const val KEY_TRANSPORTE = "transporte"
        private const val KEY_BAJO_CARBONO = "bajo_carbono"
        private const val KEY_VIAJE_GRUPAL = "viaje_grupal"
        private const val KEY_NOMBRE = "nombre_viajero"

        // Factory para inyectar DataStoreManager al ViewModel
        fun factory(dataStoreManager: DataStoreManager): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val savedStateHandle = extras.createSavedStateHandle()
                    return EcoTripViewModel(
                        savedStateHandle = savedStateHandle,
                        dataStoreManager = dataStoreManager
                    ) as T
                }
            }
        }
    }

    // Estado inicial: restaura desde SavedStateHandle si hubo Process Death
    private val _uiState = MutableStateFlow(
        EcoTripUiState(
            nombreViajero = savedStateHandle[KEY_NOMBRE] ?: "",
            destino = savedStateHandle[KEY_DESTINO] ?: "",
            duracionTexto = savedStateHandle[KEY_DURACION] ?: "",
            transporte = savedStateHandle[KEY_TRANSPORTE] ?: "",
            viajeBajoCarbono = savedStateHandle[KEY_BAJO_CARBONO] ?: false,
            esViajeGrupal = savedStateHandle[KEY_VIAJE_GRUPAL] ?: false
        )
    )

    val uiState: StateFlow<EcoTripUiState> = _uiState.asStateFlow()

    // Al arrancar, carga desde DataStore las preferencias persistidas en disco
    // Solo aplica si SavedStateHandle no tiene valor (primera apertura de la app)
    init {
        viewModelScope.launch {
            // Carga nombre del viajero desde DataStore
            dataStoreManager.usernameFlow.collect { nombre ->
                if (savedStateHandle.get<String>(KEY_NOMBRE).isNullOrEmpty()) {
                    _uiState.update { it.copy(nombreViajero = nombre) }
                }
            }
        }
        viewModelScope.launch {
            // Carga preferencia de huella de carbono desde DataStore
            dataStoreManager.lowCarbonPreferenceFlow.collect { lowCarbon ->
                if (savedStateHandle.get<Boolean>(KEY_BAJO_CARBONO) == null) {
                    _uiState.update { it.copy(viajeBajoCarbono = lowCarbon) }
                }
            }
        }
    }

    // --- Actualizadores de estado ---

    // Nombre del viajero: persiste en DataStore (sobrevive cierre total)
    fun actualizarNombreViajero(valor: String) {
        savedStateHandle[KEY_NOMBRE] = valor
        _uiState.update { it.copy(nombreViajero = valor, mensajeError = null) }
        viewModelScope.launch {
            dataStoreManager.saveUsername(valor)
        }
    }

    // Destino: persiste en SavedStateHandle (sobrevive rotación y Process Death)
    fun actualizarDestino(valor: String) {
        savedStateHandle[KEY_DESTINO] = valor
        _uiState.update { it.copy(destino = valor, mensajeError = null) }
    }

    // Duración: solo dígitos, persiste en SavedStateHandle
    fun actualizarDuracion(valor: String) {
        val textoLimpio = valor.filter { it.isDigit() }
        savedStateHandle[KEY_DURACION] = textoLimpio
        _uiState.update { it.copy(duracionTexto = textoLimpio, mensajeError = null) }
    }

    // Transporte: persiste en SavedStateHandle
    fun actualizarTransporte(valor: String) {
        savedStateHandle[KEY_TRANSPORTE] = valor
        _uiState.update { it.copy(transporte = valor, mensajeError = null) }
    }

    // Huella de carbono: persiste en DataStore (preferencia global permanente)
    fun actualizarViajeBajoCarbono(valor: Boolean) {
        savedStateHandle[KEY_BAJO_CARBONO] = valor
        _uiState.update { it.copy(viajeBajoCarbono = valor, mensajeError = null) }
        viewModelScope.launch {
            dataStoreManager.saveLowCarbonPreference(valor)
        }
    }

    // Viaje grupal: persiste en SavedStateHandle
    fun actualizarViajeGrupal(valor: Boolean) {
        savedStateHandle[KEY_VIAJE_GRUPAL] = valor
        _uiState.update { it.copy(esViajeGrupal = valor, mensajeError = null) }
    }

    // --- Validación ---

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val duracion = estado.duracionTexto.toIntOrNull()

        val mensaje = when {
            estado.nombreViajero.isBlank() -> "Ingrese su nombre."
            estado.destino.isBlank() -> "Ingrese un destino válido."
            duracion == null || duracion <= 0 -> "Ingrese una duración mayor a cero."
            estado.transporte.isBlank() -> "Seleccione un tipo de transporte."
            else -> null
        }

        _uiState.update { it.copy(mensajeError = mensaje) }
        return mensaje == null
    }

    // Construye el modelo de dominio desde el estado actual
    fun obtenerPreferenciaViaje(): PreferenciaViaje {
        val estado = _uiState.value
        return PreferenciaViaje(
            nombreViajero = estado.nombreViajero.trim(),
            destino = estado.destino.trim(),
            duracionDias = estado.duracionTexto.toIntOrNull() ?: 0,
            transporte = estado.transporte,
            viajeBajoCarbono = estado.viajeBajoCarbono,
            esViajeGrupal = estado.esViajeGrupal
        )
    }

    fun limpiarFormulario() {
        savedStateHandle[KEY_NOMBRE] = ""
        savedStateHandle[KEY_DESTINO] = ""
        savedStateHandle[KEY_DURACION] = ""
        savedStateHandle[KEY_TRANSPORTE] = ""
        savedStateHandle[KEY_BAJO_CARBONO] = false
        savedStateHandle[KEY_VIAJE_GRUPAL] = false
        _uiState.value = EcoTripUiState()
    }
}