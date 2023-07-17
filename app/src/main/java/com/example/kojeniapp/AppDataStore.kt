package com.example.kojeniapp

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class AppDataStore constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        const val DATA_STORE_NAME = "app_data_store"
    }

    object Keys {
        val NEXT_FEEDING_HOUR = intPreferencesKey(name = "next_feeding_hour")
        val NEW_FEEDING_DIALOG = booleanPreferencesKey(name = "use_dialog")
        val HISTORY_LENGTH = intPreferencesKey(name = "history_length")
    }

    object Defaults {
        const val NEXT_FEEDING_HOUR = 2
        const val NEW_FEEDING_DIALOG = true
        const val HISTORY_LENGTH = 5
    }

    val settings: Flow<AppSettings> = dataStore.data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            Log.e("AppDataStore", "Error reading preferences.", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        AppSettings(
            nextFeedingHour = preferences[Keys.NEXT_FEEDING_HOUR] ?: Defaults.NEXT_FEEDING_HOUR,
            newFeedingDialog = preferences[Keys.NEW_FEEDING_DIALOG] ?: Defaults.NEW_FEEDING_DIALOG,
            historyLength = preferences[Keys.HISTORY_LENGTH] ?: Defaults.HISTORY_LENGTH
        )
    }

    suspend fun setNextFeedingHour(nextFeedingHour: Int) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[Keys.NEXT_FEEDING_HOUR] = nextFeedingHour
        }
    }

    suspend fun setNewFeedingDialog(useNewDialog: Boolean) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[Keys.NEW_FEEDING_DIALOG] = useNewDialog
        }
    }

    suspend fun setHistoryLength(historyLength: Int) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[Keys.HISTORY_LENGTH] = historyLength
        }
    }
}

data class AppSettings(
    val nextFeedingHour: Int = AppDataStore.Defaults.NEXT_FEEDING_HOUR,
    val newFeedingDialog: Boolean = AppDataStore.Defaults.NEW_FEEDING_DIALOG,
    val historyLength: Int = AppDataStore.Defaults.HISTORY_LENGTH
)
