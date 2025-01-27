package com.dicoding.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.remote.ApiConfig
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _filteredEvents = MutableLiveData<List<ListEventsItem>>()
    val filteredEvents: LiveData<List<ListEventsItem>> = _filteredEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var allEvents: List<ListEventsItem> = emptyList()

    fun fetchFinishedEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getListEventsItem(0)
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        allEvents = response.body()?.listEvents ?: emptyList()
                        _events.value = allEvents
                        _filteredEvents.value = allEvents
                    }
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                }
            })
        }
    }

    fun searchEvents(query: String) {
        val filtered = allEvents.filter { event ->
            event.name.contains(query, ignoreCase = true) || event.description.contains(query, ignoreCase = true)
        }
        _filteredEvents.value = filtered
    }
}
