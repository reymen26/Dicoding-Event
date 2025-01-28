package com.dicoding.dicodingevent.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
    Dengan ViewModelFactory, Anda dapat memasukkan constructor dengan cara mengirim data ke VIewModelFactory
    terlebih dahulu, baru setelah itu dikirimkan ke ViewModel pada fungsi create.
*/
class ViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}