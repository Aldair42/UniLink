package com.example.unilink.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.unilink.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "unilink_session"
)

class AndroidSessionStorage(
    context: Context
) : SessionStorage {
    private val appContext = context.applicationContext

    override suspend fun getSavedUser(): User? {
        return appContext.sessionDataStore.data.map { preferences ->
            val id = preferences[USER_ID] ?: return@map null
            val email = preferences[USER_EMAIL].orEmpty()
            if (email.isBlank()) {
                null
            } else {
                User(
                    id = id,
                    name = preferences[USER_NAME].orEmpty(),
                    email = email,
                    password = ""
                )
            }
        }.first()
    }

    override suspend fun saveUser(user: User) {
        appContext.sessionDataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
        }
    }

    override suspend fun clearSession() {
        appContext.sessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private companion object {
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }
}
