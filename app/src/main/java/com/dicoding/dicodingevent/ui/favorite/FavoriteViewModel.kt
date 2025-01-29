package com.dicoding.dicodingevent.ui.favorite

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application, private val repository: FavoriteRepository) : ViewModel() {

    private val _currentEvent = MutableLiveData<Lifecycle.Event>()
    val currentEvent: LiveData<Lifecycle.Event> = _currentEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _events = MutableLiveData<List<EventEntity>>()
    val events: LiveData<List<EventEntity>> get() = _events


    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> get() = _isFavorited

    fun setCurrentEvent(event: Lifecycle.Event) {
        _currentEvent.value = event
    }

    // Cek apakah event sudah ada di favorit berdasarkan ID
    fun getFavoriteById(id: String) = viewModelScope.launch {
        _isFavorited.value = repository.getFavoriteById(id) != null
    }

    fun getAllFavorites() = viewModelScope.launch {
        _isLoading.value = true
        _events.value = repository.getAllFavorites()
        _isLoading.value = false
    }

    // Menambahkan event ke favorit
    fun addFavorite(event: EventEntity) = viewModelScope.launch {
        repository.insertFavorite(event)
        _isFavorited.value = true
    }

    fun updateFavorite(event: EventEntity) = viewModelScope.launch {
        repository.updateFavorite(event)
        _isFavorited.value = true
    }

    fun deleteFavorite(event: EventEntity) = viewModelScope.launch {
        repository.deleteFavorite(event)
        _isFavorited.value = false
    }
}