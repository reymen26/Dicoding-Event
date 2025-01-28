package com.dicoding.dicodingevent.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        // asLiveData merupakan fungsi yang digunakan untuk mengubah Flow menjadi LiveData.
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActivity: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActivity)
        }
    }
}