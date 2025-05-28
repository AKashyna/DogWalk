package com.example.dogwalk.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow


private val Context.dataStore by preferencesDataStore(name = "theme_prefs")

class ThemePreferenceManager(private val context: Context) {
    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
    }

    val selectedTheme: Flow<AppTheme> = context.dataStore.data.map { prefs ->
        val name = prefs[THEME_KEY] ?: AppTheme.LIGHT.name
        AppTheme.valueOf(name)
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }
}
