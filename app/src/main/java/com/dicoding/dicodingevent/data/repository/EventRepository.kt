package com.dicoding.dicodingevent.data.repository

import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.local.room.EventDao

class EventRepository private constructor(private val eventDao: EventDao) {

    // Mengambil semua data favorit
    suspend fun getAllFavorites(): List<EventEntity> {
        return eventDao.getAllFavorites()
    }

    suspend fun getFavoriteById(id: String): EventEntity? {
        return eventDao.getFavoriteEventById(id)
    }

    suspend fun insertFavorite(favoriteEvent: EventEntity) {
        eventDao.insertFavorite(favoriteEvent)
    }

    suspend fun deleteFavorite(favoriteEvent: EventEntity) {
        eventDao.deleteFavorite(favoriteEvent)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(dao: EventDao): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(dao).also { instance = it }
            }
    }
}