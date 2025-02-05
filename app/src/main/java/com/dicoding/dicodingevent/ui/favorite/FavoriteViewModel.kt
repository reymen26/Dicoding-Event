package com.dicoding.dicodingevent.ui.favorite

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application, private val repository: EventRepository) : ViewModel() {

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

    fun getAllFavorites() = viewModelScope.launch {
        _isLoading.value = true
        _events.value = repository.getAllFavorites()
        _isLoading.value = false
    }
}