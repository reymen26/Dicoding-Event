package com.dicoding.dicodingevent.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/*
    Pada kode di bawah, kita membuat extension properties pada Context dengan nama dataStore yang dibuat
    dengan menggunakan property delegation by preferencesDataStore. Property delegation adalah sebuah
    mekanisme untuk mendelegasikan suatu tugas kepada class lain.
*/
class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    /*
    Menyimpan Data ke Preferences DataStore. Untuk menyimpan data, yang Anda perlukan hanyalah instance
    DataStore dan Key pada SettingPreferences.
    */
    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    /*
        Membaca Data dari Preferences DataStore. Sama seperti proses menyimpan, yang Anda perlukan
        untuk membaca data hanyalah instance DataStore dan Key pada SettingPreferences.
    */
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}