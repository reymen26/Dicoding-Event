package com.dicoding.dicodingevent.viewModelFavtory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.ui.detail.DetailEventViewModel
import com.dicoding.dicodingevent.ui.favourite.FavouriteViewModel

class DetailEventViewModelFactory(
    private val application: Application,
    private val repository: EventRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailEventViewModel::class.java)) {
            return DetailEventViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FavoriteViewModelFactory(
    private val application: Application,
    private val repository: EventRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
