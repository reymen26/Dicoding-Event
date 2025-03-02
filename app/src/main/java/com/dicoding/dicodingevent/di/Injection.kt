package com.dicoding.dicodingevent.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dicoding.dicodingevent.data.local.room.EventDatabase
import com.dicoding.dicodingevent.data.remote.ApiService
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.ui.favourite.FavouriteViewModel
import com.dicoding.dicodingevent.viewModelFavtory.FavoriteViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {

    private const val BASE_URL = "https://event-api.dicoding.dev/"

    // Fungsi untuk menyediakan instance ApiService
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun provideFavoriteViewModel(owner: ViewModelStoreOwner, context: Context): FavouriteViewModel {
        val database = EventDatabase.getInstance(context)
        val eventRepository = EventRepository.getInstance(database.eventDao())
        val factory =
            FavoriteViewModelFactory(context.applicationContext as Application, eventRepository)
        return ViewModelProvider(owner, factory).get(FavouriteViewModel::class.java)
    }
}