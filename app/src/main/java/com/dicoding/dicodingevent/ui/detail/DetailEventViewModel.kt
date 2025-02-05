package com.dicoding.dicodingevent.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.ListEventsItem
import kotlinx.coroutines.launch

class DetailEventViewModel(application: Application, private val repository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> get() = _event

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> get() = _isFavorited

    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getFavoriteById(id: String) = viewModelScope.launch {
        _isFavorited.value = repository.getFavoriteById(id) != null
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun addFavorite(event: EventEntity) = viewModelScope.launch {
        repository.insertFavorite(event)
        _isFavorited.value = true
    }

    fun deleteFavorite(event: EventEntity) = viewModelScope.launch {
        repository.deleteFavorite(event)
        _isFavorited.value = false
    }
}