package com.example.ecotrip_2026.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import androidx.datastore.preferences.core.emptyPreferences

private val Context.dataStore by preferencesDataStore(name = "ecotrip_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val LOW_CARBON_KEY = booleanPreferencesKey("low_carbon_preference")
    }

    val usernameFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USERNAME_KEY] ?: ""
        }

    val lowCarbonPreferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[LOW_CARBON_KEY] ?: false
        }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username.trim()
        }
    }

    suspend fun saveLowCarbonPreference(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOW_CARBON_KEY] = enabled
        }
    }

    suspend fun saveUserPreferences(
        username: String,
        lowCarbonPreference: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username.trim()
            preferences[LOW_CARBON_KEY] = lowCarbonPreference
        }
    }

    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}