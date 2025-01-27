package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.ListEventsItem

class DetailEventViewModel : ViewModel() {

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> get() = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun setEvent(event: ListEventsItem) {
        _event.value = event
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}
