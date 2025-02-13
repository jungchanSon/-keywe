package com.ssafy.keywe.data.datastore

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "profile_prefs")


class ProfileDataStore @Inject constructor(
    private val context: Context
) {
    private object PreferencesKeys {
        val PROFILE_ID = longPreferencesKey("profile_id")
    }

    val profileIdFlow: Flow<Long?> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(androidx.datastore.preferences.core.emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[PreferencesKeys.PROFILE_ID]
    }

    suspend fun saveProfileId(profileId: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROFILE_ID] = profileId
        }
    }

    suspend fun clearProfileId() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.PROFILE_ID)
        }
    }
}